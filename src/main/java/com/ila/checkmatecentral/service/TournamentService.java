package com.ila.checkmatecentral.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ila.checkmatecentral.entity.Match;
import com.ila.checkmatecentral.entity.MatchStatus;
import com.ila.checkmatecentral.entity.Tournament;
import com.ila.checkmatecentral.entity.TournamentStatus;
import com.ila.checkmatecentral.entity.UserAccount;
import com.ila.checkmatecentral.exceptions.PlayerAlreadyInTournamentException;
import com.ila.checkmatecentral.exceptions.TournamentNotFoundException;
import com.ila.checkmatecentral.repository.TournamentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final UserAccountService userAccountService;
    private final MatchService matchService;

    public Tournament create(Tournament tournament) {
        final Date now = new Date();
        
        if (tournament.getStartDate().after(tournament.getEndDate())) {
            return null;
        }
        
        tournament.setCreateDate(now);
        tournament.setRound(1);

        updateTournamentStatus(tournament);

        this.tournamentRepository.save(tournament);
        return tournament;
    }

    public void delete(Tournament tournament) {
        this.tournamentRepository.delete(tournament);
    }

    public void deleteById(Integer id) {
        this.tournamentRepository.deleteById(id);
    }

    public List<Tournament> getAllTournaments() {
        return this.tournamentRepository.findAll();
    }

    public Tournament getTournament(Integer id) {
        return this.tournamentRepository.findById(id).orElseThrow(() -> new TournamentNotFoundException(id));
    }

    public Tournament update(Integer tournamentId, Tournament updatedTournament) {
        Tournament existingTournament = this.tournamentRepository.findById(tournamentId)
            .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        existingTournament.setName(updatedTournament.getName());
        existingTournament.setDescription(updatedTournament.getDescription());
        existingTournament.setType(updatedTournament.getType());
        existingTournament.setMaxPlayers(updatedTournament.getMaxPlayers());
        existingTournament.setMinElo(updatedTournament.getMinElo());
        existingTournament.setStartDate(updatedTournament.getStartDate());
        existingTournament.setEndDate(updatedTournament.getEndDate());
        existingTournament.setCreateDate(new Date());

        updateTournamentStatus(existingTournament);
        return this.tournamentRepository.save(existingTournament);
    }

    public void addPlayer(Integer tournamentId, Long playerId) throws PlayerAlreadyInTournamentException{
        Tournament currentTournament = this.tournamentRepository.findById(tournamentId).orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        List<UserAccount> playerList = getPlayers(tournamentId);
        List<Long> playerListIds = new ArrayList<Long>();
        for (UserAccount player : playerList) {
            playerListIds.add(player.getId());
        }
        UserAccount player = userAccountService.loadUserById(playerId);
        if (playerListIds.contains(playerId)) {
            throw new PlayerAlreadyInTournamentException(tournamentId);
        }
        currentTournament.addPlayer(player);
        tournamentRepository.save(currentTournament);
    }

    public List<UserAccount> getPlayers(Integer tournamentId) {
        Tournament currentTournament = this.tournamentRepository
                .findById(tournamentId).orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        return currentTournament.getPlayerList();
    }
    
    private static void updateTournamentStatus(Tournament tournament) {
        if (tournament.getStartDate().before(new Date())) {
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
                        .toList();

        return winners;
    }

    private static int getHighestRound(List<Match> matches) {
        return matches.stream()
                .mapToInt(Match::getRound)
                .max()
                .orElse(0);
    }

    public ResponseEntity<?> setNextRound(int tournamentId){
        final List<Match> matches = matchService.getMatches(tournamentId);

        if (!matches.stream().allMatch(match -> match.getMatchStatus() == MatchStatus.COMPLETED)) {
            return ResponseEntity.status(HttpStatus.OK).body("Matches are not complete");
        }

        final List<UserAccount> winners = getLastMatchWinners(matches);

        if (winners.size() == 1) {
            endTournament(tournamentId);
            return ResponseEntity.status(HttpStatus.OK).body("Tournament has ended");
        }

        int highestRound = getHighestRound(matches);
        matchService.createMatches(winners, highestRound + 1, tournamentId);
        return ResponseEntity.status(HttpStatus.OK).body("Next round has started");
    }

    private void endTournament(int tournamentId) {
        Tournament tournament = getTournament(tournamentId);
        tournament.setStatus(TournamentStatus.COMPLETED);
        tournamentRepository.save(tournament);
    }
}
