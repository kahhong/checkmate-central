package com.ila.checkmatecentral.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ila.checkmatecentral.entity.Admin;
import com.ila.checkmatecentral.entity.Match;
import com.ila.checkmatecentral.entity.MatchStatus;
import com.ila.checkmatecentral.entity.Player;
import com.ila.checkmatecentral.entity.Tournament;
import com.ila.checkmatecentral.entity.TournamentStatus;
import com.ila.checkmatecentral.exceptions.InvalidNumberOfPlayersException;
import com.ila.checkmatecentral.exceptions.InvalidTournamentException;
import com.ila.checkmatecentral.exceptions.InvalidTournamentStateException;
import com.ila.checkmatecentral.exceptions.PlayerAlreadyInTournamentException;
import com.ila.checkmatecentral.exceptions.PlayerNotInTournamentException;
import com.ila.checkmatecentral.exceptions.TournamentFullException;
import com.ila.checkmatecentral.exceptions.TournamentNotFoundException;
import com.ila.checkmatecentral.repository.TournamentRepository;
import com.ila.checkmatecentral.utility.MathUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final AccountCredentialService accountCredentialService;
    private final MatchService matchService;

    public Tournament create(Tournament tournament, String adminPrincipal) {
        final LocalDateTime now = LocalDateTime.now();
        
        int numPlayers = tournament.getMaxPlayers();

        if (numPlayers <= 0 || !MathUtils.isPowerOf2(numPlayers)){
            throw new InvalidNumberOfPlayersException(numPlayers);
        }
        
        if (tournament.getStartDate().isAfter(tournament.getEndDate())) {
            throw new InvalidTournamentException("End date must be before start date");
        }
        
        tournament.setLastUpdated(now);
        tournament.setRound(1);

        Admin admin = (Admin) accountCredentialService.loadUserByUsername(adminPrincipal);
        tournament.setAdmin(admin);

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

    public void addPlayer(int tournamentId, Player player) throws PlayerAlreadyInTournamentException {
        final Tournament currentTournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        if (currentTournament.getStatus() != TournamentStatus.UPCOMING) {
            throw new InvalidTournamentStateException(
                "Tournament ID: " + tournamentId + " has been started, Player added Unsuccessful");
        }

        final List<Player> players = currentTournament.getPlayerList();
        if (players.size() >= currentTournament.getMaxPlayers()) {
            throw new TournamentFullException(tournamentId);
        }

        if (player.getTournament() != null) {
            throw new PlayerAlreadyInTournamentException(tournamentId);
        }

        currentTournament.addPlayer(player);
        tournamentRepository.save(currentTournament);
    }

    @Transactional
    public void withdrawPlayer(int tournamentId, Player player) {
        final Tournament currentTournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        List<Player> players = currentTournament.getPlayerList();

        if (!players.contains(player)) {
            throw new PlayerNotInTournamentException(tournamentId);
        }

        players.remove(player);
        currentTournament.setPlayerList(players);
        tournamentRepository.save(currentTournament);

        List<Match> matches = matchService.getMatches(currentTournament.getTournamentId());
        for (Match match : matches) {
            if (match.getMatchStatus() == MatchStatus.ONGOING) {
                matchService.withdrawPlayer(match, player);
            }
        }

    }
    
    private static void updateTournamentStatus(Tournament tournament) {
        if (tournament.getStartDate().isBefore(LocalDateTime.now())) {
            tournament.setStatus(TournamentStatus.ONGOING);
        } else {
            tournament.setStatus(TournamentStatus.UPCOMING);
        }
    }

    private List<Player> getPlayersLeft(Tournament tournament) {
        List<Player> players = tournament.getPlayerList();
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

    public void pairUp(List<Player> players, int tournamentId){
        /* 
         * Match Specifications
         * - Bye matches are required if the number of players in tournaments are not a power of two
         * - We generate bye matches to eliminate players until a number of players in tournament is a power of 2
         * - Every match will decrease player by 1 => so we need bye matches for every extra player
         *
         * Case: 9 players
         *
         * Let R be extra players. R should be 1
         * 9 players = 8 matched + R
         * 9 players = 2^floor(log2(9 players)) + R
         *
         * R = 9 players - 2^floor(log2(9 players))
         *   = 9 players - 2^floor(3.169...)
         *   = 9 players - 2^3
         *   = 9 players - 8
         *   = 1 player
         *
         * PROVEN R = 1
         * === 
         * Case: 7 players
         *
         * Let R be extra players. R should be 3
         * 7 players = 4 matched + R
         * 7 players = 2^floor(log2(7 players)) + R
         *
         * R = 7 players - 2^floor(log2(7 players))
         *   = 7 players - 4
         *   = 3 players
         *
         * PROVEN R = 3
         */
        
        int neededMatches = players.size() / 2;

        if (!MathUtils.isPowerOf2(players.size())) {
            int nearestPowerOfTwo = (int) Math.pow(2, MathUtils.log2(players.size()));
            neededMatches = players.size() - nearestPowerOfTwo;
        }

        int round = getTournament(tournamentId).getRound();

        List<Player> sortedPlayers = players.stream()
                .sorted((player1, player2) -> Double.compare(player1.getRating(), player2.getRating()))
                .toList();
        
        for (int i = 0; i < neededMatches; i++) {
            Player lowEloPlayer = sortedPlayers.get(i);
            Player highEloPlayer = sortedPlayers.get(players.size() - 1 - i);
            matchService.createSingleMatch(lowEloPlayer, highEloPlayer, round, tournamentId);
        }
    }

    public Tournament setNextRound(int tournamentId) {
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

        List<Player> playersLeft = getPlayersLeft(tournament);

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
        List<Player> players = tournament.getPlayerList();
        
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
