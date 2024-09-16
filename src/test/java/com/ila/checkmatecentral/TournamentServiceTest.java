package com.ila.checkmatecentral;

import com.ila.checkmatecentral.entity.Tournament;
import com.ila.checkmatecentral.entity.TournamentType;
import com.ila.checkmatecentral.form.TournamentCreateForm;
import com.ila.checkmatecentral.service.TournamentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class TournamentServiceTest {

    @Autowired
    TournamentService service;

    @Test
    public void canCreateTournament() {
        TournamentCreateForm mockTournamentForm = new TournamentCreateForm();
        mockTournamentForm.setName("Hello World");
        mockTournamentForm.setDescription("A test tournament");
        mockTournamentForm.setMaxPlayers(10);
        mockTournamentForm.setMinElo(0);
        mockTournamentForm.setType(TournamentType.SINGLE_KNOCKOUT);
        mockTournamentForm.setStartDate(new Date());

        Tournament createdTournament = service.create(mockTournamentForm);

        assertThat(createdTournament).isNotNull();
        assertThat(createdTournament.getName()).isEqualTo("Hello World");
        assertThat(createdTournament.getDescription()).isEqualTo("A test tournament");
    }
}
