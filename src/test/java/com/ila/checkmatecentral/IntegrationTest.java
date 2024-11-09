package com.ila.checkmatecentral;

import com.ila.checkmatecentral.entity.TournamentType;
import com.ila.checkmatecentral.entity.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpServletResponse;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    static private String adminToken;
    static private String userToken;

    @BeforeAll
    static void setUp(@Autowired MockMvc mockMvc) throws Exception {
        UserAccount user = new UserAccount("user@gmail.com", "user", "password", "ROLE_USER");
        UserAccount admin = new UserAccount("admin@gmail.com", "admin", "password", "ROLE_ADMIN");
        userToken = getUserToken(user, mockMvc);
        adminToken = getUserToken(admin, mockMvc);
    }
    
    @Test
    static String getUserToken(UserAccount user, MockMvc mockMvc) throws Exception {
        final JSONObject userJSON = new JSONObject()
            .put("name", user.getName())
            .put("email", user.getEmail())
            .put("password", user.getPassword())
            .put("grantedAuthorityString", user.getGrantedAuthorityString());

        mockMvc.perform(post("/api/auth/register")
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