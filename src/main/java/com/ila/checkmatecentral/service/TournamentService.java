package com.ila.checkmatecentral.service;

import com.ila.checkmatecentral.entity.Player;
import com.ila.checkmatecentral.entity.Tournament;
import com.ila.checkmatecentral.entity.TournamentStatus;
import com.ila.checkmatecentral.form.TournamentCreateForm;
import com.ila.checkmatecentral.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TournamentService {
    private final PlayerRepository playerRepository;

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

        return newTournament;
    }
}
