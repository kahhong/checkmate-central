package com.ila.checkmatecentral.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.ila.checkmatecentral.entity.Player;
import com.ila.checkmatecentral.service.PlayerService;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/player")
public class PlayerController {
    public final PlayerService playerService;

    // get Player Availability
    @GetMapping({ "/{playerId}/availability" })
    public ResponseEntity<?> getAvailability(@PathVariable("playerId") Long playerId) {
        Player player = playerService.getPlayer(playerId);
        
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("availability", player.isAvailability()));
    }
    
    @PutMapping("/{playerId}/availability")
    public ResponseEntity<?> updateAvailability(@PathVariable("playerId") Long playerId, @RequestBody JsonNode json) {
        Player player = playerService.getPlayer(playerId);
        String availabilityText = json.get("availability").asText();
        player = playerService.updateAvailability(player, availabilityText);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
            "message", "Player availability has been updated",
            "availability", player.isAvailability()
        ));
    }

    // get Player description
    @GetMapping({ "/{playerId}/profile" })
    public ResponseEntity<?> getProfile(@PathVariable("playerId") Long playerId) {
        Player player = playerService.getPlayer(playerId);

        player.eraseCredentials();
        
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message", "message",
                    "player", player));
    }

    @PutMapping("/{playerId}/updateProfile")
    public ResponseEntity<?> updateProfileDescription(@PathVariable("playerId") Long playerId,
            @RequestBody JsonNode json) {

        String description = json.get("description").asText();
        Player player = playerService.getPlayer(playerId);
        playerService.updateProfileDescription(player,description);
        return ResponseEntity.status(HttpStatus.OK).body(
            Map.of("message", "Player description updated successfully"));
    }

    @GetMapping("/{playerId}/getRank")
    public ResponseEntity<?> getRank(@PathVariable("playerId") Long playerId,
            @RequestBody JsonNode json) {

        Player player = playerService.getPlayer(playerId);
        long count = playerService.getNumberOfPlayersWithHigherRating(player)+1;

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "message", "Count retrieved successfully",
                "count", count));
    }
    
}
