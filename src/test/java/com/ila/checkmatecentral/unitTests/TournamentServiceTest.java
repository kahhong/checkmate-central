package com.ila.checkmatecentral.unitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ila.checkmatecentral.entity.*;
import com.ila.checkmatecentral.service.AccountCredentialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import com.ila.checkmatecentral.exceptions.InvalidTournamentStateException;
import com.ila.checkmatecentral.exceptions.PlayerAlreadyInTournamentException;
import com.ila.checkmatecentral.exceptions.TournamentNotFoundException;
import com.ila.checkmatecentral.repository.TournamentRepository;
import com.ila.checkmatecentral.service.MatchService;
import com.ila.checkmatecentral.service.TournamentService;

@SpringBootTest
public class TournamentServiceTest {

    @InjectMocks
    private TournamentService tournamentService;

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private MatchService matchService;

    @Mock
    private AccountCredentialService credentialService;

    private Tournament tournament;

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
    }

    @Test
    void testCreateTournament_InvalidNumberOfPlayers() {
        ReflectionTestUtils.setField(tournament, "maxPlayers", 5);
        when(credentialService.loadUserByUsername(any(String.class))).thenReturn(new Admin("mockAdmin@test.com", "mockAdmin", "mockpassword"));

        assertThrows(IllegalArgumentException.class, () -> tournamentService.create(tournament, "mockAdmin@test.com"),
                "Number of players must be power of 2");

        verifyNoInteractions(tournamentRepository);
    }

    @Test
    void testCreateTournament_ValidNumberOfPlayers() {
        ReflectionTestUtils.setField(tournament, "maxPlayers", 8);
        when(credentialService.loadUserByUsername(any(String.class))).thenReturn(new Admin("mockAdmin@test.com", "mockAdmin", "mockpassword"));
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);

        Tournament createdTournament = tournamentService.create(tournament, "mockAdmin@test.com");

        assertNotNull(createdTournament);
        assertEquals(8, createdTournament.getMaxPlayers());

        verify(tournamentRepository, times(1)).save(tournament);
    }

    @Test
    void testAddPlayer_PlayerAlreadyInTournament() {
        // Given: Mock player is already in the tournament's player list
        Player player = new Player("user@mail.com", "User", "password");
        ReflectionTestUtils.setField(player, "Id", 1L);

        tournament.addPlayer(player);
        tournament.setStatus(TournamentStatus.UPCOMING);

        when(tournamentRepository.findById(any(Integer.class))).thenReturn(Optional.of(tournament));

        assertThrows(PlayerAlreadyInTournamentException.class,
            () -> tournamentService.addPlayer(tournament.getTournamentId(), player));

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

    @Test
    void testSetNextRound_MatchesNotCompleted() {
        // Given: Single incomplete match in tournament
        when(tournamentRepository.findById(tournament.getTournamentId())).thenReturn(Optional.of(tournament));
        when(matchService
                .getMatches(tournament.getTournamentId()))
                .thenReturn(List.of(
                    new Match(null, null, null, 1, tournament.getTournamentId())));

        assertThrows(InvalidTournamentStateException.class,
                () -> tournamentService.setNextRound(1),
                "Not all matches are completed");
    }

    @Test
    void testDeleteTournament() {
        doNothing().when(tournamentRepository).deleteById(1);

        tournamentService.deleteById(1);

        verify(tournamentRepository, times(1)).deleteById(1);
    }
}
