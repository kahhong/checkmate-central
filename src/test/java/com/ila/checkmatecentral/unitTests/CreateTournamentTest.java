package com.ila.checkmatecentral.unitTests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ila.checkmatecentral.controller.TournamentController;
import com.ila.checkmatecentral.entity.TournamentType;
import com.ila.checkmatecentral.service.AccountCredentialService;
import com.ila.checkmatecentral.service.MatchService;
import com.ila.checkmatecentral.service.PlayerService;
import com.ila.checkmatecentral.service.TournamentService;

/**
 * This test class tests the validation mechanisms of user input data fields in the Tournament class
 * (@NotNull, @NotEmpty, @Range etc...)
 * by mocking up malformed JSONs and sending them to POST /tournaments/.
 */
@WebMvcTest(TournamentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CreateTournamentTest {
    private static String END_POINT = "/api/tournaments/";

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private TournamentService tournamentService;

    @MockBean
    private AccountCredentialService credentialService;

    @MockBean
    private MatchService matchService;

    @MockBean
    private PlayerService playerService;

    private static JSONObject createJsonTemplate() throws JSONException {
        return new JSONObject()
                .put("name", "Tournament") // Empty tournament name
                .put("description", "A description")
                .put("type", TournamentType.SINGLE_KNOCKOUT.name())
                .put("maxPlayers", 10)
                .put("minElo", 1000)
                .put("startDate", LocalDateTime.now().plusDays(1))
                .put("endDate", LocalDateTime.now().plusDays(2));
    }

    @Test
    public void testCreateTournament_EmptyName_ReturnsBadRequest() throws Exception {
        String json = createJsonTemplate().put("name", "").toString();

        mockMvc.perform(post(END_POINT)
                .contentType("application/json")
                .content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateTournament_EmptyDescription_ReturnsBadRequest() throws Exception {
        String json = createJsonTemplate().put("description", "").toString();

        mockMvc.perform(post(END_POINT)
                .contentType("application/json")
                .content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateTournament_NullType_ReturnsBadRequest() throws Exception {
        String json = createJsonTemplate().put("type", null).toString();

        mockMvc.perform(post(END_POINT)
                .contentType("application/json")
                .content(json))
            .andExpect(status().isBadRequest());
    }

    public void testCreateTournament_MaxPlayersLessThan2_ReturnsBadRequest() throws Exception {
        String json = createJsonTemplate().put("maxPlayers", 1).toString();

        mockMvc.perform(post(END_POINT)
                .contentType("application/json")
                .content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateTournament_MaxPlayersMoreThan100_ReturnsBadRequest() throws Exception {
        String json = createJsonTemplate().put("maxPlayers", 101).toString();

        mockMvc.perform(post(END_POINT)
                .contentType("application/json")
                .content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateTournament_NegativeElo_ReturnsBadRequest() throws Exception {
        String json = createJsonTemplate().put("minElo", -1).toString();

        mockMvc.perform(post(END_POINT)
                .contentType("application/json")
                .content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateTournament_NonISOStartDate_ReturnsBadRequest() throws Exception {
        String json = createJsonTemplate().put("startDate", "Invalid").toString();

        mockMvc.perform(post(END_POINT)
                .contentType("application/json")
                .content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateTournament_NonISOEndDate_ReturnsBadRequest() throws Exception {
        String json = createJsonTemplate().put("endDate", "Invalid").toString();

        mockMvc.perform(post(END_POINT)
                .contentType("application/json")
                .content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateTournament_StartDateInThePast_ReturnsBadRequest() throws Exception {
        String json = createJsonTemplate().put("startDate", LocalDateTime.now().minusDays(1)).toString();

        mockMvc.perform(post(END_POINT)
                .contentType("application/json")
                .content(json))
            .andExpect(status().isBadRequest());
    }
}
