package com.ila.checkmatecentral.unitTests;

import com.ila.checkmatecentral.entity.TournamentType;
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
        //TODO
    }
}
