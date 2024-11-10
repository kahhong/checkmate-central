package com.ila.checkmatecentral.unitTests;

import static org.assertj.core.api.AssertionsForClassTypes.withinPercentage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import com.ila.checkmatecentral.entity.Tournament;
import com.ila.checkmatecentral.entity.TournamentStatus;
import com.ila.checkmatecentral.entity.TournamentType;
import com.ila.checkmatecentral.entity.Player;
import com.ila.checkmatecentral.exceptions.PlayerAlreadyInTournamentException;
import com.ila.checkmatecentral.exceptions.TournamentNotFoundException;
import com.ila.checkmatecentral.repository.TournamentRepository;
import com.ila.checkmatecentral.service.MatchService;
import com.ila.checkmatecentral.service.TournamentService;

@SpringBootTest
public class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private UserAccountService userAccountService;

    @Mock
    private MatchService matchService;

    @InjectMocks
    private TournamentService tournamentService;

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
        ReflectionTestUtils.setField(tournament, "startDate", LocalDateTime.now());
        ReflectionTestUtils.setField(tournament, "endDate", LocalDateTime.now());
        ReflectionTestUtils.setField(tournament, "status", TournamentStatus.ONGOING);
        ReflectionTestUtils.setField(tournament, "lastUpdated", LocalDateTime.now());
        ReflectionTestUtils.setField(tournament, "round", 1);

        // Mocking an empty list for matches and players
        ReflectionTestUtils.setField(tournament, "matches", new ArrayList<>());
        ReflectionTestUtils.setField(tournament, "playerList", new ArrayList<>());



        player = new Player("test@example.com", "Test User", "password123");
    }

    @Test
    void testCreateTournament_InvalidNumberOfPlayers() {
        // Arrange
        Tournament invalidTournament = new Tournament();
        ReflectionTestUtils.setField(invalidTournament, "name", "Invalid Tournament");
        ReflectionTestUtils.setField(invalidTournament, "description", "Tournament with invalid number of players");
        ReflectionTestUtils.setField(invalidTournament, "type", TournamentType.SINGLE_KNOCKOUT); // Enum
        ReflectionTestUtils.setField(invalidTournament, "maxPlayers", 5); // Invalid number of players (not 2^n)
        ReflectionTestUtils.setField(invalidTournament, "minElo", 1000);
        ReflectionTestUtils.setField(invalidTournament, "startDate", LocalDateTime.now());
        ReflectionTestUtils.setField(invalidTournament, "endDate", LocalDateTime.now());
        ReflectionTestUtils.setField(invalidTournament, "status", TournamentStatus.UPCOMING);
        ReflectionTestUtils.setField(invalidTournament, "lastUpdated", LocalDateTime.now());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            tournamentService.create(invalidTournament);
        });
        assertEquals("Number of players must be power of 2", exception.getMessage());

        // Verify no save operation is called
        verify(tournamentRepository, times(0)).save(any(Tournament.class));
    }

    @Test
    void testCreateTournament_ValidNumberOfPlayers() {
        // Arrange
        Tournament validTournament = new Tournament();
        ReflectionTestUtils.setField(validTournament, "name", "Valid Tournament");
        ReflectionTestUtils.setField(validTournament, "description", "Tournament with valid number of players");
        ReflectionTestUtils.setField(validTournament, "type", TournamentType.SINGLE_KNOCKOUT); // Enum
        ReflectionTestUtils.setField(validTournament, "maxPlayers", 8); // Valid number of players (2^n)
        ReflectionTestUtils.setField(validTournament, "minElo", 1000);
        ReflectionTestUtils.setField(validTournament, "startDate", LocalDateTime.now());
        ReflectionTestUtils.setField(validTournament, "endDate", LocalDateTime.now());
        ReflectionTestUtils.setField(validTournament, "status", TournamentStatus.UPCOMING);
        ReflectionTestUtils.setField(validTournament, "lastUpdated", LocalDateTime.now());

        // Mock behavior of the repository save
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(validTournament);

        // Act
        Tournament createdTournament = tournamentService.create(validTournament);

        // Assert
        assertNotNull(createdTournament); // The created tournament should not be null
        assertEquals(8, createdTournament.getMaxPlayers()); // Ensure the number of players is correct

        // Verify save operation is called exactly once
        verify(tournamentRepository, times(1)).save(validTournament);
    }

    @Test
    void testAddPlayer_PlayerAlreadyInTournament() {
        // Given: Mock player is already in the tournament's player list
        tournament.addPlayer(player);
        tournament.setStatus(TournamentStatus.UPCOMING);

        // Mocking repository and service calls
        when(tournamentRepository.findById(any(Integer.class))).thenReturn(Optional.of(tournament));

        // When & Then: Expect PlayerAlreadyInTournamentException
        assertThrows(PlayerAlreadyInTournamentException.class,
            () -> tournamentService.addPlayer(tournament.getTournamentId(), player) // Trying to add the same user
        );

        // Verify interactions with the mocked repository
        verify(tournamentRepository, times(1)).findById(any(Integer.class));
    }

    @Test
    void testGetTournament_TournamentNotFound() {
        // Given: No tournament exists for the given ID
        when(tournamentRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        // When & Then: Expect TournamentNotFoundException
        assertThrows(TournamentNotFoundException.class, () -> {
            tournamentService.getTournament(2);
        });
    }

    /*
     * TODO: Smth wrong with the implementation for this case
     */
    /* 
    @Test
    void testGetPlayers_NoPlayersInTournament() {
        // Given: Tournament with no players
        when(tournamentRepository.findById(any(Integer.class))).thenReturn(Optional.of(tournament));

        // When: Calling getPlayers
        List<UserAccount> players = tournamentService.getPlayers();

        // Then: The returned player list should be empty
        assertTrue(players.isEmpty());
    }
        */

    // @Test
    // void testSetNextRound_MatchesNotCompleted() {
    //     // Given: Mocking incomplete matches for the current round
    //     when(tournamentRepository.findById(any(Integer.class))).thenReturn(Optional.of(tournament));
    //     when(matchService.getMatches(any(Integer.class))).thenReturn(List.of(
    //             new Match(MatchStatus.PENDING),
    //             new Match(MatchStatus.COMPLETED)));

    //     // When & Then: Expect MatchesNotCompletedException
    //     assertThrows(MatchesNotCompletedException.class, () -> {
    //         tournamentService.setNextRound(1);
    //     });
    // }

    @Test
    void testDeleteTournament() {
        doNothing().when(tournamentRepository).deleteById(1);

        tournamentService.deleteById(1);

        verify(tournamentRepository, times(1)).deleteById(1);
    }


}
