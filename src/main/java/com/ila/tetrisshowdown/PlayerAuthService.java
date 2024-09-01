package com.ila.tetrisshowdown;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerAuthService {
    @Autowired
    private PlayerRepository playerRepository;

    public Player registerPlayer(Player player) {
        return playerRepository.save(player);
    }

    public boolean playerExists(String email) {
        // Check if a player with the given email exists
        return playerRepository.findByEmail(email).isPresent();
    }

    // Login: Check if the provided email and password are correct
    public Optional<Player> loginPlayer(String email, String password) {
        Optional<Player> player = playerRepository.findByEmail(email);
        if (player.isPresent() && player.get().getPassword().equals(password)) {
            return player;
        }
        return Optional.empty();
    }
}
