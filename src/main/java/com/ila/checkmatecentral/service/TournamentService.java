package com.ila.checkmatecentral.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

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
    private final TournamentRepository tournamentRepository;
    private final MatchService matchService;

    public Tournament create(Tournament tournament) {
        final LocalDateTime now = LocalDateTime.now();
        
        int numPlayers = tournament.getMaxPlayers();

        if (numPlayers > 0 && (numPlayers & (numPlayers - 1)) != 0){
            throw new InvalidNumberOfPlayersException();
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

    private List<UserAccount> getPlayersLeft(Tournament tournament) {
        List<UserAccount> players = tournament.getPlayerList();
        List<Match> matches = matchService.getMatches(tournament.getTournamentId());
        for (Match match : matches) {
            players.remove(match.getLoser());
        }
        return players;
    }

    private boolean allCompleted(List<Match> matches){
        for (Match match : matches) {
            if (match.getMatchStatus() != MatchStatus.COMPLETED){
                return false;
            }
        }
        return true;
    }

    public void pairUp(List<UserAccount> players, int tournamentId){
        List<UserAccount> sortedPlayers = players.stream()
                .sorted((user1, user2) -> Double.compare(user1.getRating(), user2.getRating()))
                .collect(Collectors.toList());
        int neededMatches = (int) Math.ceil(players.size()/2);

        if(!isPowerOf2(neededMatches)){
            neededMatches = log2(neededMatches) + 1;
        }

        int round = getTournament(tournamentId).getRound();

        
        for (int i = 0; i < neededMatches; i++) {
            UserAccount lowEloPlayer = sortedPlayers.get(i);
            UserAccount highEloPlayer = sortedPlayers.get(players.size() - 1 - i);
            matchService.createSingleMatch(lowEloPlayer, highEloPlayer, round, tournamentId);
        }
    }
    
    public static boolean isPowerOf2(int num){
        return num > 0 && (num & (num - 1)) != 0;
    }

    public static int log2(int x) {
        return (int) (Math.log(x) / Math.log(2));
    }

    public Tournament setNextRound(int tournamentId){
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        if(tournament.getStatus() == TournamentStatus.COMPLETED){
            throw new InvalidTournamentStateException(
                "Tournament is completed");
        }

        List<Match> matches = matchService.getMatches(tournamentId);

        if(!allCompleted(matches)){
            throw new InvalidTournamentStateException(
                "Not all matches are completed");
        }

        List<UserAccount> playersLeft = getPlayersLeft(tournament);

        if (playersLeft.size() == 1) {
            endTournament(tournamentId);
            throw new InvalidTournamentStateException(
                "Tournament has ended");
        }

        tournament.setRound(tournament.getRound()+1);
        tournamentRepository.save(tournament);

        pairUp(playersLeft, tournamentId);
        return tournament;
    }



    public void startTournament(int tournamentId) throws InvalidTournamentStateException {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        List<UserAccount> players = tournament.getPlayerList();
        
        // if (players.size() != tournament.getMaxPlayers()) {
        //     throw new InsufficientPlayersException(players.size(), tournament.getMaxPlayers());
        // }
        
        if (tournament.getStatus() != TournamentStatus.UPCOMING) {
            throw new InvalidTournamentStateException("Tournament has already started");
        }

        tournament.setStatus(TournamentStatus.ONGOING);
        pairUp(players, tournamentId);
    }

    private void endTournament(int tournamentId) {
        Tournament tournament = getTournament(tournamentId);
        tournament.setStatus(TournamentStatus.COMPLETED);
        tournamentRepository.save(tournament);
    }
}
