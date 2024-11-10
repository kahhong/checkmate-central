package com.ila.checkmatecentral.unitTests;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import com.ila.checkmatecentral.entity.Tournament;
import com.ila.checkmatecentral.entity.TournamentStatus;
import com.ila.checkmatecentral.entity.TournamentType;
import com.ila.checkmatecentral.entity.Player;
import com.ila.checkmatecentral.repository.TournamentRepository;
import com.ila.checkmatecentral.service.MatchService;
import com.ila.checkmatecentral.service.TournamentService;

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
    private Player player;

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

        player = new Player("test@example.com", "Test User", "password123");
    }


}
