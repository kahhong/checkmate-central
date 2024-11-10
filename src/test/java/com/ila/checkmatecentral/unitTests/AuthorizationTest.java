package com.ila.checkmatecentral.unitTests;

import com.ila.checkmatecentral.config.SecurityConfig;
import com.ila.checkmatecentral.controller.TournamentController;
import com.ila.checkmatecentral.entity.TournamentType;
import com.ila.checkmatecentral.service.MatchService;
import com.ila.checkmatecentral.service.TournamentService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TournamentController.class)
@Import(SecurityConfig.class)
public class AuthorizationTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TournamentService tournamentService;

    @Mock
    private UserAccountService userAccountService;

    @Mock
    private MatchService matchService;


    @Test
    @WithMockUser
    public void testCreateTournament_withUser_returnsUnauthorized() throws Exception {
        mockMvc.perform(post("/api/tournaments/")
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

}
