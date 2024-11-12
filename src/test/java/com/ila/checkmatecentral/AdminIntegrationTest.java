package com.ila.checkmatecentral;

import com.ila.checkmatecentral.entity.Admin;
import com.ila.checkmatecentral.entity.TournamentType;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    static private String adminToken;

    @BeforeAll
    static void setUp(@Autowired MockMvc mockMvc) throws Exception {
        // Setup admin account in database
        Admin admin = new Admin("admin@gmail.com", "admin", "password");
        adminToken = getAdminToken(admin, mockMvc);
    }

    static String getAdminToken(Admin user, MockMvc mockMvc) throws Exception {
        final JSONObject userJSON = new JSONObject()
            .put("name", user.getName())
            .put("email", user.getEmail())
            .put("password", user.getPassword());

        mockMvc.perform(post("/api/auth/register/admin")
                .contentType("application/json")
                .content(userJSON.toString()));

        final JSONObject loginJSON = new JSONObject()
            .put("email", user.getEmail())
            .put("password", user.getPassword());

        MockHttpServletResponse loginResponse = mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(loginJSON.toString()))
                .andReturn()
                .getResponse();

        JSONObject responseBody = new JSONObject(loginResponse.getContentAsString());
        return responseBody.getString("token");
    }

    @Test
    void testAdminCreateTournament() throws Exception {
        mockMvc.perform(post("/api/tournaments/")
        .contentType("application/json")
        .header("Authorization", "Bearer " + adminToken)
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
    void addPlayersToTournament(){// TournamentController, JWT token, TournamentService

    }

    @Test
    void testAdminStartTournament(){ // TournamentController, JWT token, TournamentService

    }

    @Test
    void testAdminUpdateAndNextRoundTournament(){ // TournamentController, JWT token, TournamentService, matchService
        
    }

}