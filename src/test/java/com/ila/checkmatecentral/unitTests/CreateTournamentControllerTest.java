package com.ila.checkmatecentral.unitTests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.ila.checkmatecentral.config.SecurityConfig;
import com.ila.checkmatecentral.controller.TournamentController;
import com.ila.checkmatecentral.repository.TournamentRepository;
import com.ila.checkmatecentral.repository.UserAccountRepository;
import com.ila.checkmatecentral.service.MatchService;
import com.ila.checkmatecentral.service.TournamentService;
import com.ila.checkmatecentral.service.UserAccountService;

// TODO: Eventually when we decide on the exact error messages, we will add them into the tests

/**
 * This test class tests the validation mechanisms of user input data fields in the Tournament class
 * (@NotNull, @NotEmpty, @Range etc...)
 * by mocking up malformed JSONs and sending them to POST /tournaments/.
 */
@WebMvcTest(TournamentController.class)
@Import(SecurityConfig.class)
public class CreateTournamentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // TODO: Remove these when the dependencies are removed from TournamentController.class
    @MockBean
    private TournamentService tournamentService;

    @MockBean
    private TournamentRepository tournamentRepository;

    @MockBean
    private MatchService matchService;

    @MockBean
    private UserAccountService userAccountService;

    @MockBean
    private UserAccountRepository userAccountRepository;

    @Test
    public void testCreateTournament_EmptyName_ReturnsBadRequest() throws Exception {
        // Arrange
        JSONObject json = new JSONObject();
        json.put("name", "");      // Empty tournament name
        json.put("description", "A description");
        json.put("type", "SINGLE KNOCKOUT");
        json.put("maxPlayers", "10");
        json.put("minElo", "1000");
        json.put("startDate", "2024-10-01T15:35:41Z");
        json.put("endDate", "2024-11-01T15:35:41Z");

        // Action and Assert
        mockMvc.perform(post("/tournaments/")
                .contentType("application/json")
                .content(json.toString()))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateTournament_EmptyDescription_ReturnsBadRequest() throws Exception {
        // Arrange
        JSONObject json = new JSONObject();
        json.put("name", "A new tournament");
        json.put("description", "");    // Empty description
        json.put("type", "SINGLE KNOCKOUT");
        json.put("maxPlayers", "10");
        json.put("minElo", "1000");
        json.put("startDate", "2024-10-01T15:35:41Z");
        json.put("endDate", "2024-11-01T15:35:41Z");

        // Action and Assert
        mockMvc.perform(post("/tournaments/")
                .contentType("application/json")
                .content(json.toString()))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateTournament_NullType_ReturnsBadRequest() throws Exception {
        // Arrange
        JSONObject json = new JSONObject();
        json.put("name", "A new tournament");
        json.put("description", "A description");
        json.put("type", null);             // Null type
        json.put("maxPlayers", "10");
        json.put("minElo", "1000");
        json.put("startDate", "2024-10-01T15:35:41Z");
        json.put("endDate", "2024-11-01T15:35:41Z");

        // Action and Assert
        mockMvc.perform(post("/tournaments/")
                .contentType("application/json")
                .content(json.toString()))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateTournament_MaxPlayersLessThan2_ReturnsBadRequest() throws Exception {
        // Arrange
        JSONObject json = new JSONObject();
        json.put("name", "A new tournament");
        json.put("description", "A description");
        json.put("type", "SINGLE_KNOCKOUT");
        json.put("maxPlayers", "1");
        json.put("minElo", "1000");
        json.put("startDate", "2024-10-01T15:35:41Z");
        json.put("endDate", "2024-11-01T15:35:41Z");

        // Action and Assert
        mockMvc.perform(post("/tournaments/")
                .contentType("application/json")
                .content(json.toString()))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateTournament_MaxPlayersMoreThan100_ReturnsBadRequest() throws Exception {
        // Arrange
        JSONObject json = new JSONObject();
        json.put("name", "A new tournament");
        json.put("description", "A description");
        json.put("type", "SINGLE_KNOCKOUT");
        json.put("maxPlayers", "101");
        json.put("minElo", "1000");
        json.put("startDate", "2024-10-01T15:35:41Z");
        json.put("endDate", "2024-11-01T15:35:41Z");

        // Action and Assert
        mockMvc.perform(post("/tournaments/")
                .contentType("application/json")
                .content(json.toString()))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateTournament_NegativeElo_ReturnsBadRequest() throws Exception {
        // Arrange
        JSONObject json = new JSONObject();
        json.put("name", "A new tournament");
        json.put("description", "A description");
        json.put("type", "SINGLE_KNOCKOUT");
        json.put("maxPlayers", "10");
        json.put("minElo", "-1");
        json.put("startDate", "2024-10-01T15:35:41Z");
        json.put("endDate", "2024-11-01T15:35:41Z");

        // Action and Assert
        mockMvc.perform(post("/tournaments/")
                .contentType("application/json")
                .content(json.toString()))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateTournament_NonISOStartDate_ReturnsBadRequest() throws Exception {
        // Arrange
        JSONObject json = new JSONObject();
        json.put("name", "A new tournament");
        json.put("description", "A description");
        json.put("type", "SINGLE_KNOCKOUT");
        json.put("maxPlayers", "10");
        json.put("minElo", "1000");
        json.put("startDate", "today is the day");
        json.put("endDate", "2024-11-01T15:35:41Z");

        // Action and Assert
        mockMvc.perform(post("/tournaments/")
                .contentType("application/json")
                .content(json.toString()))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateTournament_NonISOEndDate_ReturnsBadRequest() throws Exception {
        // Arrange
        JSONObject json = new JSONObject();
        json.put("name", "A new tournament");
        json.put("description", "A description");
        json.put("type", "SINGLE_KNOCKOUT");
        json.put("maxPlayers", "10");
        json.put("minElo", "1000");
        json.put("startDate", "2024-10-01T15:35:41Z");
        json.put("endDate", "Gg guys");

        // Action and Assert
        mockMvc.perform(post("/tournaments/")
                .contentType("application/json")
                .content(json.toString()))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateTournament_StartDateInThePast_ReturnsBadRequest() throws Exception {
        // Arrange
        JSONObject json = new JSONObject();
        json.put("name", "A new tournament");
        json.put("description", "A description");
        json.put("type", "SINGLE_KNOCKOUT");
        json.put("maxPlayers", "10");
        json.put("minElo", "1000");
        json.put("startDate", "1965-08-09T15:35:41Z");  // 1965 is earlier than today()
        json.put("endDate", "2024-10-01T15:35:41Z");

        // Action and Assert
        mockMvc.perform(post("/tournaments/")
                .contentType("application/json")
                .content(json.toString()))
            .andExpect(status().isBadRequest());
    }

    // TODO: Fail if endDate is earlier or equal to startDate
}
