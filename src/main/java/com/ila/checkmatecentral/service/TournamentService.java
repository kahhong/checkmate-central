package com.ila.checkmatecentral.service;

import java.util.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import org.hibernate.engine.internal.Collections;
import org.hibernate.mapping.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ila.checkmatecentral.entity.Match;
import com.ila.checkmatecentral.entity.MatchStatus;
import com.ila.checkmatecentral.entity.Tournament;
import com.ila.checkmatecentral.entity.TournamentStatus;
import com.ila.checkmatecentral.entity.UserAccount;
import com.ila.checkmatecentral.exceptions.InsufficientPlayersException;
import com.ila.checkmatecentral.exceptions.InvalidNumberOfPlayersException;
import com.ila.checkmatecentral.exceptions.InvalidTournamentException;
import com.ila.checkmatecentral.exceptions.InvalidTournamentStateException;
import com.ila.checkmatecentral.exceptions.PlayerAlreadyInTournamentException;
import com.ila.checkmatecentral.exceptions.TournamentFullException;
import com.ila.checkmatecentral.exceptions.TournamentNotFoundException;
import com.ila.checkmatecentral.repository.TournamentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class TournamentService {
    private static final Logger logger = LoggerFactory.getLogger(TournamentService.class);

    private final TournamentRepository tournamentRepository;
    private final MatchService matchService;

    public Tournament create(Tournament tournament) {
        final LocalDateTime now = LocalDateTime.now();
        
        int numPlayers = tournament.getMaxPlayers();

        if (numPlayers > 0 && (numPlayers & (numPlayers - 1)) != 0){
            //throw new InvalidNumberOfPlayersException();
        }
        
        if (tournament.getStartDate().isAfter(tournament.getEndDate())) {
            throw new InvalidTournamentException("End date must be before start date");
        }
        
        tournament.setLastUpdated(now);
        tournament.setRound(1);

        updateTournamentStatus(tournament);

        return tournamentRepository.save(tournament);
    }

    public void delete(Tournament tournament) {
        this.tournamentRepository.delete(tournament);
    }

    public void deleteById(int id) {
        this.tournamentRepository.deleteById(id);
    }

    public List<Tournament> getAllTournaments() {
        return this.tournamentRepository.findAll();
    }

    public Tournament getTournament(int id) {
        return this.tournamentRepository.findById(id).orElseThrow(() -> new TournamentNotFoundException(id));
    }
    
    public boolean exists(int id) {
        return tournamentRepository.findById(id).isPresent();
    }

    public Tournament update(int tournamentId, Tournament updatedTournament) {
        Tournament existingTournament = this.tournamentRepository.findById(tournamentId)
            .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        existingTournament.setName(updatedTournament.getName());
        existingTournament.setDescription(updatedTournament.getDescription());
        existingTournament.setType(updatedTournament.getType());
        existingTournament.setMaxPlayers(updatedTournament.getMaxPlayers());
        existingTournament.setMinElo(updatedTournament.getMinElo());
        existingTournament.setStartDate(updatedTournament.getStartDate());
        existingTournament.setEndDate(updatedTournament.getEndDate());
        existingTournament.setLastUpdated(LocalDateTime.now());

        updateTournamentStatus(existingTournament);
        return tournamentRepository.save(existingTournament);
    }

    public void addPlayer(int tournamentId, UserAccount player) throws PlayerAlreadyInTournamentException {
        final Tournament currentTournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        if (currentTournament.getStatus() != TournamentStatus.UPCOMING) {
            throw new InvalidTournamentStateException(
                "Tournament ID: " + tournamentId + " has been started, Player added Unsuccessful");
        }

        final List<UserAccount> players = currentTournament.getPlayerList();
        if (players.size() >= currentTournament.getMaxPlayers()) {
            throw new TournamentFullException(tournamentId);
        }

        if (players.stream().anyMatch(tournamentPlayer -> tournamentPlayer.getId() == player.getId())) {
            throw new PlayerAlreadyInTournamentException(tournamentId);
        }

        currentTournament.addPlayer(player);
        tournamentRepository.save(currentTournament);
    }
    
    private static void updateTournamentStatus(Tournament tournament) {
        if (tournament.getStartDate().isBefore(LocalDateTime.now())) {
            tournament.setStatus(TournamentStatus.ONGOING);
        } else {
            tournament.setStatus(TournamentStatus.UPCOMING);
        }
    }

    private static List<UserAccount> getLastMatchWinners(List<Match> matches) {
        int highestRound = getHighestRound(matches);

        List<UserAccount> winners = matches.stream()
                        .filter(match -> match.getRound() == highestRound)
                        .map(Match::getWinnerSK)
                        .collect(Collectors.toList());

        return winners;
    }

    private static int getHighestRound(List<Match> matches) {
        return matches.stream()
                .mapToInt(Match::getRound)
                .max()
                .orElse(0);
    }


    public ResponseEntity<?> setNextRound(int tournamentId){
        Tournament tournament = getTournament(tournamentId);
        final List<Match> matches = matchService.getMatches(tournamentId);

        if (!matches.stream().allMatch(match -> match.getMatchStatus() == MatchStatus.COMPLETED)) {
            return ResponseEntity.status(HttpStatus.OK).body("Matches are not complete");
        }

        List<UserAccount> winners = getLastMatchWinners(matches);

        // add all the byesPlayers into the winner list
        List<UserAccount> byesPlayers = tournament.getByesPlayers();
        if(!byesPlayers.isEmpty()){
            for(UserAccount byesPlayer : byesPlayers){
                winners.add(byesPlayer);
            }
        }

        // remove all the byesPlayers;
        List<UserAccount> emptyByesPlayers = new ArrayList<>();
        tournament.setByesPlayers(emptyByesPlayers);
        tournamentRepository.save(tournament);


        if (winners.size() == 1) {
            endTournament(tournamentId);
            return ResponseEntity.status(HttpStatus.OK).body("Tournament has ended");
        }

        //int highestRound = getHighestRound(matches);
        // Increment tournament's round by 1
        tournament.setRound(tournament.getRound()+1);
        tournamentRepository.save(tournament);
        if(!isPowerN(winners.size())){
            List<UserAccount> byesPlayer= eliminationRound(winners, tournament.getRound(), tournamentId);
            for(UserAccount player : byesPlayer){
                tournament.addByesPlayer(player);
            }
            tournamentRepository.save(tournament);
            return ResponseEntity.status(HttpStatus.OK).body("Next round has started");
        }else{
            matchService.createMatches(winners, tournament.getRound(), tournamentId);
            return ResponseEntity.status(HttpStatus.OK).body("Next round has started");
        }

    }

    public List<UserAccount> getPlayers(int tournamentId) {
        Tournament currentTournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        return currentTournament.getPlayerList();
    }

    public void startTournament(int tournamentId) throws InvalidTournamentStateException {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        List<UserAccount> players = tournament.getPlayerList();
        if (tournament.getStatus() != TournamentStatus.UPCOMING) {
            throw new InvalidTournamentStateException("Tournament has already started");
        }
        
        if(!isPowerN(players.size())){
            List<UserAccount> byesPlayer= eliminationRound(players, tournament.getRound(), tournamentId);
            for(UserAccount player : byesPlayer){
                tournament.addByesPlayer(player);
            }
            tournamentRepository.save(tournament);
        }else{
            tournament.setStatus(TournamentStatus.ONGOING);
            matchService.createMatches(players, tournament.getRound(), tournamentId);
        }
        
    }

    private void endTournament(int tournamentId) {
        Tournament tournament = getTournament(tournamentId);
        tournament.setStatus(TournamentStatus.COMPLETED);
        tournamentRepository.save(tournament);
    }

    private boolean isPowerN(int numPlayers){
        return numPlayers > 0 && (numPlayers & (numPlayers - 1)) != 0;
    }

    private int findNextPowerN(int numPlayers){
        if (numPlayers <= 1) {
            throw new IllegalArgumentException("numPlayers must be greater than 1");
        }

        // Start with 1 (which is 2^0)
        int powerOfTwo = 1;
        
        // Double powerOfTwo until it's larger than or equal to the given numPlayers
        while (powerOfTwo * 2 < numPlayers) {
            powerOfTwo *= 2;
        }

        return powerOfTwo;
    }

    private List<UserAccount> eliminationRound(List<UserAccount> players, int round, int tournamentId){

        int numPlayers = players.size();
        int nextPower = findNextPowerN(numPlayers);
        int numOfElimMatch = numPlayers-nextPower;

        // Sort players by ELO in descending order
        List<UserAccount> sortedPlayers = players.stream()
            .sorted((user1, user2) -> Double.compare(user1.getRating(), user2.getRating()))
            .collect(Collectors.toList());
        // Find which players are to go elimination round
        List<UserAccount> elimPlayers = findEliminationPlayers(sortedPlayers, numOfElimMatch);
        logger.info("ELIMINATION ROUND WORKKKIIINNNNGGGG");
        matchService.createMatches(elimPlayers, round, tournamentId);
        List<UserAccount> byesPlayers = findByesPlayers(sortedPlayers, elimPlayers);
        for(UserAccount byesPlayer : byesPlayers){
            logger.info("BYESSSSSS" + byesPlayer.getUsername());
        }

        return byesPlayers;
    }

    public static List<UserAccount> findEliminationPlayers(List<UserAccount> sortedPlayers, int numOfElimMatches) {
        int numOfPlayersToEliminate = numOfElimMatches * 2;  // Total players for elimination matches

        // Group players by their Elo ratings (same Elo ratings will be grouped together)
        java.util.Map<Double, List<UserAccount>> playersByElo = sortedPlayers.stream()
            .collect(Collectors.groupingBy(UserAccount::getRating, TreeMap::new, Collectors.toList()));

        // A list to collect selected players for elimination
        List<UserAccount> selectedPlayers = new ArrayList<>();

        // Iterate over the players grouped by Elo (lowest Elo to highest, since TreeMap is sorted by key)
        for (java.util.Map.Entry<Double, List<UserAccount>> entry : playersByElo.entrySet()) {
            List<UserAccount> currentEloGroup = entry.getValue();
            int playersRemainingToSelect = numOfPlayersToEliminate - selectedPlayers.size();

            // If the number of players in this Elo group is less than or equal to what we need
            if (currentEloGroup.size() <= playersRemainingToSelect) {
                // Add all players from this Elo group
                selectedPlayers.addAll(currentEloGroup);
            } else {
                // If we have more players in this Elo group than needed, randomly select the required number
                java.util.Collections.shuffle(currentEloGroup);  // Shuffle for randomness
                selectedPlayers.addAll(currentEloGroup.subList(0, playersRemainingToSelect)); // Select only needed players
            }

            // Break if we have selected enough players for elimination
            if (selectedPlayers.size() >= numOfPlayersToEliminate) {
                break;
            }
        }

        return selectedPlayers;
    }

    public static List<UserAccount> findByesPlayers(List<UserAccount> players, List<UserAccount> elimPlayers) {
        // Filter players who are not in the eliminated set
        List<UserAccount> byesPlayers = players.stream()
                .filter(player -> !elimPlayers.contains(player))
                .collect(Collectors.toList());
        for(UserAccount player : byesPlayers){
            logger.info("BYEEESSSSS:" + player.getEmail());
        }
        return byesPlayers;
    }



}
