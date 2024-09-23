package com.ila.checkmatecentral.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.ila.checkmatecentral.entity.Tournament;
import com.ila.checkmatecentral.entity.TournamentStatus;
import com.ila.checkmatecentral.entity.UserAccount;
import com.ila.checkmatecentral.exceptions.TournamentNotFoundException;
import com.ila.checkmatecentral.form.TournamentCreateForm;
import com.ila.checkmatecentral.repository.TournamentRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Slf4j
public class TournamentService {
    private final TournamentRepository tournamentRepository;

    public Tournament create(TournamentCreateForm tournamentCreateForm) {
        Tournament newTournament = new Tournament();
        newTournament.setName(tournamentCreateForm.getName());
        newTournament.setDescription(tournamentCreateForm.getDescription());
        newTournament.setType(tournamentCreateForm.getType());
        newTournament.setMaxPlayers(tournamentCreateForm.getMaxPlayers());
        newTournament.setMinElo(tournamentCreateForm.getMinElo());
        newTournament.setStartDate(tournamentCreateForm.getStartDate());
        newTournament.setEndDate(tournamentCreateForm.getEndDate());
        newTournament.setCreateDate(LocalDateTime.now());
        newTournament.setRound(1);

        Instant currentDate = new Date().toInstant();
        if(newTournament.getStartDate().toInstant().isBefore(currentDate)) {
            newTournament.setStatus(TournamentStatus.ONGOING);
        } else {
            newTournament.setStatus(TournamentStatus.UPCOMING);
        }

        this.tournamentRepository.save(newTournament);
        return newTournament;
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

    public Tournament update(Integer tournamentId, TournamentCreateForm updatedTournamentCreateForm) {
        Tournament existingTournament = this.tournamentRepository.findById(tournamentId)
            .orElseThrow(() -> new TournamentNotFoundException(tournamentId)); 

        existingTournament.setName(updatedTournamentCreateForm.getName());
        existingTournament.setDescription(updatedTournamentCreateForm.getDescription());
        existingTournament.setType(updatedTournamentCreateForm.getType());
        existingTournament.setMaxPlayers(updatedTournamentCreateForm.getMaxPlayers());
        existingTournament.setMinElo(updatedTournamentCreateForm.getMinElo());
        existingTournament.setStartDate(updatedTournamentCreateForm.getStartDate());
        existingTournament.setEndDate(updatedTournamentCreateForm.getEndDate());
        existingTournament.setCreateDate(LocalDateTime.now());

        Instant currentDate = new Date().toInstant();
        if (updatedTournamentCreateForm.getStartDate().toInstant().isBefore(currentDate)) {
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

    public void createMatches(){

    }

    public void getCurrentRoundMatches(){

    }

    public void getAllMatches(){
        
    }
}
