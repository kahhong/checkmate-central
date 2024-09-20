package com.ila.checkmatecentral.service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ila.checkmatecentral.entity.Tournament;
import com.ila.checkmatecentral.entity.TournamentStatus;
import com.ila.checkmatecentral.exceptions.TournamentNotFoundException;
import com.ila.checkmatecentral.form.TournamentCreateForm;
import com.ila.checkmatecentral.repository.TournamentRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
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

        Instant currentDate = new Date().toInstant();
        if (updatedTournamentCreateForm.getStartDate().toInstant().isBefore(currentDate)) {
            existingTournament.setStatus(TournamentStatus.ONGOING);
        } else {
            existingTournament.setStatus(TournamentStatus.UPCOMING);
        }

        return this.tournamentRepository.save(existingTournament);
    }

}
