package com.ila.checkmatecentral.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ila.checkmatecentral.entity.*;
import com.ila.checkmatecentral.exceptions.MatchesNotCompletedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.ila.checkmatecentral.exceptions.TournamentNotFoundException;
import com.ila.checkmatecentral.repository.TournamentRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Slf4j
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final MatchService matchService;

    public Tournament create(Tournament tournament) {
        tournament.setCreateDate(LocalDateTime.now());
        tournament.setRound(1);

        Instant currentDate = new Date().toInstant();
        if(tournament.getStartDate().toInstant().isBefore(currentDate)) {
            tournament.setStatus(TournamentStatus.ONGOING);
        } else {
            tournament.setStatus(TournamentStatus.UPCOMING);
        }

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
        existingTournament.setCreateDate(LocalDateTime.now());

        Instant currentDate = new Date().toInstant();
        if (updatedTournament.getStartDate().toInstant().isBefore(currentDate)) {
            existingTournament.setStatus(TournamentStatus.ONGOING);
        } else {
            existingTournament.setStatus(TournamentStatus.UPCOMING);
        }

        return this.tournamentRepository.save(existingTournament);
    }

    public void addPlayer(Integer tournamentId, UserAccount player){
        Tournament currentTournament = this.tournamentRepository.findById(tournamentId).orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        currentTournament.addPlayer(player);
        tournamentRepository.save(currentTournament);
    }

    public List<UserAccount> getPlayers(Integer tournamentId) {
        Tournament currentTournament = this.tournamentRepository.findById(tournamentId).orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        return currentTournament.getPlayerList();
    }

    public List<UserAccount> getWinners(Integer tournamentId) {
        // 1) get the match list from tournamentId
        // 2) check if the round is completed
        // 3) get highest round number
        // 4) iteratrate all high round number matches -  and add winner to list of player
        // 5) call createMatch
        List<Match> matches = matchService.getMatches(tournamentId);
        boolean matchesCompleted = matches.stream()
                .allMatch(match -> match.getMatchStatus() == MatchStatus.COMPLETED);

        if(matchesCompleted) {
            int highestRound = matches.stream()
                    .mapToInt(Match::getRound)
                    .max()
                    .orElse(0);

            List<UserAccount> winners = new ArrayList<>(
                    matches.stream()
                            .filter(match -> match.getRound() == highestRound)
                            .map(Match::getWinnerSK)
                            .toList());

            return winners;
        }
        else{
            throw new MatchesNotCompletedException();
        }
    }

    public Integer getHighestRound(Integer tournamentId) {
        List<Match> matches = matchService.getMatches(tournamentId);
        boolean matchesCompleted = matches.stream()
                .allMatch(match -> match.getMatchStatus() == MatchStatus.COMPLETED);

        int highestRound = 0;
        if (matchesCompleted) {
            highestRound = matches.stream()
                    .mapToInt(Match::getRound)
                    .max()
                    .orElse(0);
        }
        return highestRound;
    }

    public void createMatches(){

    }

    public void getCurrentRoundMatches(){

    }

    public void getAllMatches(){
        
    }
}
