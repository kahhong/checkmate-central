package com.ila.tetrisshowdown;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    public Player registerPlayer(Player player) {
        // Ensure default values are set
        player.setRating(0); 
        player.setAvailability(false);
        return playerRepository.save(player);
    }

    // Check if an email is already registered
    public Optional<Player> findByEmail(String email) {
        return playerRepository.findByEmail(email);
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
