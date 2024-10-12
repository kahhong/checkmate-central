package com.ila.checkmatecentral.unitTests;

import com.ila.checkmatecentral.entity.Tournament;
import com.ila.checkmatecentral.entity.TournamentStatus;
import com.ila.checkmatecentral.entity.TournamentType;
import com.ila.checkmatecentral.entity.UserAccount;
import com.ila.checkmatecentral.entity.Match;
import com.ila.checkmatecentral.entity.MatchStatus;
import com.ila.checkmatecentral.exceptions.MatchesNotCompletedException;
import com.ila.checkmatecentral.exceptions.PlayerAlreadyInTournamentException;
import com.ila.checkmatecentral.exceptions.TournamentNotFoundException;
import com.ila.checkmatecentral.repository.TournamentRepository;
import com.ila.checkmatecentral.service.MatchService;
import com.ila.checkmatecentral.service.TournamentService;
import com.ila.checkmatecentral.service.UserAccountService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MatchServiceTest {
    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private UserAccountService userAccountService;

    @Mock
    private TournamentService tournamentService;

    @InjectMocks
    private MatchService matchService;

    private Tournament tournament;
    private UserAccount userAccount;

    @BeforeEach
    void setUp() {
        // Initialize the Tournament object
        tournament = new Tournament();

        // Use ReflectionTestUtils to set the tournamentId
        ReflectionTestUtils.setField(tournament, "tournamentId", 1);

        // Use ReflectionTestUtils to set private fields without using the constructor or setter
        ReflectionTestUtils.setField(tournament, "name", "Mock Tournament");
        ReflectionTestUtils.setField(tournament, "description", "Mock Description");
        ReflectionTestUtils.setField(tournament, "type", TournamentType.SINGLE_KNOCKOUT); // Enum
        ReflectionTestUtils.setField(tournament, "maxPlayers", 8);
        ReflectionTestUtils.setField(tournament, "minElo", 1000);
        ReflectionTestUtils.setField(tournament, "startDate", new Date());
        ReflectionTestUtils.setField(tournament, "endDate", new Date());
        ReflectionTestUtils.setField(tournament, "status", TournamentStatus.ONGOING);
        ReflectionTestUtils.setField(tournament, "createDate", LocalDateTime.now());
        ReflectionTestUtils.setField(tournament, "round", 1);

        // Mocking an empty list for matches and players
        ReflectionTestUtils.setField(tournament, "matches", new ArrayList<>());
        ReflectionTestUtils.setField(tournament, "playerList", new ArrayList<>());

        userAccount = new UserAccount("test@example.com", "Test User", "password123");
    }


}
