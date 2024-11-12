package com.ila.checkmatecentral.unitTests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Map;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.ila.checkmatecentral.config.SecurityConfig;
import com.ila.checkmatecentral.controller.TournamentController;
import com.ila.checkmatecentral.entity.TournamentType;
import com.ila.checkmatecentral.service.AccountCredentialService;
import com.ila.checkmatecentral.service.MatchService;
import com.ila.checkmatecentral.service.PlayerService;
import com.ila.checkmatecentral.service.TournamentService;

@WebMvcTest(TournamentController.class)
@Import(SecurityConfig.class)
public class AuthorizationTest {
    
    private static final String END_POINT = "/api/tournaments/";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TournamentService tournamentService;

    @MockBean
    private AccountCredentialService userAccountService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private MatchService matchService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateTournament_withAdmin_returnsCreated() throws Exception {
        mockMvc.perform(post(END_POINT)
                .contentType("application/json")
                .content(new JSONObject(
                    Map.of(
                        "name", "Test Tournament",
                        "description", "Test Tourney",
                        "maxPlayers", 4,
                        "minElo", 1000,
                        "type", TournamentType.SINGLE_KNOCKOUT.name(),
                        "startDate", LocalDateTime.now().plusDays(1),
                        "endDate", LocalDateTime.now().plusDays(2))
                ).toString()))
            .andExpect(status().isCreated())
            .andExpect(content().string("Tournament Created Successfully"));
    }
    
    @Test
    @WithMockUser(roles = "USER")
    void testCreateTournament_withUser_returnsUnauthorized() throws Exception {
        mockMvc.perform(post(END_POINT)
                .contentType("application/json")
                .content(new JSONObject(
                    Map.of(
                        "name", "Test Tournament",
                        "description", "Test Tourney",
                        "maxPlayers", 4,
                        "minElo", 1000,
                        "type", TournamentType.SINGLE_KNOCKOUT.name(),
                        "startDate", LocalDateTime.now().plusDays(1),
                        "endDate", LocalDateTime.now().plusDays(2))
                ).toString()))
            .andExpect(status().isForbidden());
    }

}
