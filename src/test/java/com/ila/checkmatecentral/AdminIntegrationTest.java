package com.ila.checkmatecentral;

import com.ila.checkmatecentral.entity.Admin;
import com.ila.checkmatecentral.entity.TournamentType;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    @Order(1)
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
        .andExpect(content().json("{\"message\":\"Tournament Created Successfully\"}"));
    }

    @Test
    @Order(2)
    void registerPlayersTest() throws Exception {
        // Setup player1 account in database
        final JSONObject player1JSON = new JSONObject()
                .put("name", "player1")
                .put("email", "player1@gmail.com")
                .put("password", "password");

        mockMvc.perform(post("/api/auth/register/player")
                .contentType("application/json")
                .content(player1JSON.toString()))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"message\":\"Player created successfully.\"}"));

        // Setup player2 account in database
        final JSONObject player2JSON = new JSONObject()
                .put("name", "player2")
                .put("email", "player2@gmail.com")
                .put("password", "password");

        mockMvc.perform(post("/api/auth/register/player")
                .contentType("application/json")
                .content(player2JSON.toString()))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"message\":\"Player created successfully.\"}"));

        // Setup player3 account in database
        final JSONObject player3JSON = new JSONObject()
                .put("name", "player3")
                .put("email", "player3@gmail.com")
                .put("password", "password");

        mockMvc.perform(post("/api/auth/register/player")
                .contentType("application/json")
                .content(player3JSON.toString()))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"message\":\"Player created successfully.\"}"));

        // Setup player4 account in database
        final JSONObject player4JSON = new JSONObject()
                .put("name", "player4")
                .put("email", "player4@gmail.com")
                .put("password", "password");

        mockMvc.perform(post("/api/auth/register/player")
                .contentType("application/json")
                .content(player4JSON.toString()))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"message\":\"Player created successfully.\"}"));
    }

    @Test
    @Order(3)
    void setAvailableForTournament() throws Exception {
        // Set each player's availability to true
        mockMvc.perform(put("/api/player/1/availability")
        .contentType("application/json")
        .content(new JSONObject()
        .put("availability", "True").toString()))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"message\":\"Player availability has been updated\",\"availability\":true}"));

        mockMvc.perform(put("/api/player/2/availability")
        .contentType("application/json")
        .content(new JSONObject()
        .put("availability", "True").toString()))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"message\":\"Player availability has been updated\",\"availability\":true}"));

        mockMvc.perform(put("/api/player/3/availability")
        .contentType("application/json")
        .content(new JSONObject()
        .put("availability", "True").toString()))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"message\":\"Player availability has been updated\",\"availability\":true}"));

        mockMvc.perform(put("/api/player/4/availability")
        .contentType("application/json")
        .content(new JSONObject()
        .put("availability", "True").toString()))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"message\":\"Player availability has been updated\",\"availability\":true}"));
    }

    @Test
    @Order(4)
    void addPlayersToTournament() throws Exception {
        mockMvc.perform(put("/api/tournaments/1/add")
        .contentType("application/json")
        .header("Authorization", "Bearer " + adminToken)
        .content(new JSONObject()
        .put("email", "player1@gmail.com").toString()))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"message\":\"Player Added successfully\",\"success\":true}"));

        mockMvc.perform(put("/api/tournaments/1/add")
        .contentType("application/json")
        .header("Authorization", "Bearer " + adminToken)
        .content(new JSONObject()
        .put("email", "player2@gmail.com").toString()))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"message\":\"Player Added successfully\",\"success\":true}"));

        mockMvc.perform(put("/api/tournaments/1/add")
        .contentType("application/json")
        .header("Authorization", "Bearer " + adminToken)
        .content(new JSONObject()
        .put("email", "player3@gmail.com").toString()))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"message\":\"Player Added successfully\",\"success\":true}"));

        mockMvc.perform(put("/api/tournaments/1/add")
        .contentType("application/json")
        .header("Authorization", "Bearer " + adminToken)
        .content(new JSONObject()
        .put("email", "player4@gmail.com").toString()))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"message\":\"Player Added successfully\",\"success\":true}"));
    }

    @Test
    @Order(5)
    void testAdminStartTournament() throws Exception {
        mockMvc.perform(put("/api/tournaments/1/start")
        .contentType("application/json")
        .header("Authorization", "Bearer " + adminToken))
        .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void testAdminUpdateTournament() throws Exception {
        mockMvc.perform(put("/api/match/1/update")
        .contentType("application/json")
        .header("Authorization", "Bearer " + adminToken)
        .content(new JSONObject(
            Map.of(
                "outcome", "WIN")).toString()))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"message\":\"Match 1 has been updated with WIN\"}"));

        mockMvc.perform(put("/api/match/2/update")
        .contentType("application/json")
        .header("Authorization", "Bearer " + adminToken)
        .content(new JSONObject(
            Map.of(
                "outcome", "LOSE")).toString()))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"message\":\"Match 2 has been updated with LOSE\"}"));
    }

    @Test
    @Order(7)   
    void testAdminNextRoundTournament() throws Exception {
        mockMvc.perform(put("/api/tournaments/1/nextround")
        .contentType("application/json")
        .header("Authorization", "Bearer " + adminToken))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"message\":\"Next round has started\"}"));
    }

    @Test
    @Order(8)
    void testAdminUpdateTournamentAgain() throws Exception {
        mockMvc.perform(put("/api/match/3/update")
        .contentType("application/json")
        .header("Authorization", "Bearer " + adminToken)
        .content(new JSONObject(
            Map.of(
                "outcome", "WIN")).toString()))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"message\":\"Match 3 has been updated with WIN\"}"));

        // mockMvc.perform(put("/api/match/2/update")
        // .contentType("application/json")
        // .header("Authorization", "Bearer " + adminToken)
        // .content(new JSONObject(
        //     Map.of(
        //         "outcome", "WIN")).toString()))
        // .andExpect(status().isOk())
        // .andExpect(content().json("{\"message\":\"Match 2 has been updated with WIN\"}"));
    }

    @Test
    @Order(9)
    void testGetRank() throws Exception {
        mockMvc.perform(get("/api/player/3/getRank")
        .contentType("application/json")
        .content("null"))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"message\":\"Count retrieved successfully\",\"count\":2}")); //4

        mockMvc.perform(get("/api/player/1/getRank")
        .contentType("application/json")
        .content("null"))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"message\":\"Count retrieved successfully\",\"count\":1}")); //2

        mockMvc.perform(get("/api/player/2/getRank")
        .contentType("application/json")
        .content("null"))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"message\":\"Count retrieved successfully\",\"count\":3}"));

        mockMvc.perform(get("/api/player/4/getRank")
        .contentType("application/json")
        .content("null"))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"message\":\"Count retrieved successfully\",\"count\":3}"));
    }

}