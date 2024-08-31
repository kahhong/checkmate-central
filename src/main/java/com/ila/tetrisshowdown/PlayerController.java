package com.ila.tetrisshowdown;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @PostMapping("/register")
    public ResponseEntity<String> registerPlayer(@RequestBody Player player) {
        Optional<Player> existingPlayer = playerService.loginPlayer(player.getEmail(), player.getPassword());
        if (existingPlayer.isPresent()) {
            return ResponseEntity.badRequest().body("Player with this email already exists.");
        }
        playerService.registerPlayer(player);
        return ResponseEntity.ok("Player registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginPlayer(@RequestBody Player player) {
        Optional<Player> loggedInPlayer = playerService.loginPlayer(player.getEmail(), player.getPassword());
        if (loggedInPlayer.isPresent()) {
            return ResponseEntity.ok("Login successful.");
        } else {
            return ResponseEntity.status(401).body("Invalid email or password.");
        }
    }
}
