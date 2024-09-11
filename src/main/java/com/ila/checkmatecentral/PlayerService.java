package com.ila.checkmatecentral;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;

    private float initial_rating = 0;

    public Player create(String playerName, String email, String password) {
        Player player = new Player();
        player.setPlayerName(playerName);
        player.setEmail(email);
        player.setPassword(passwordEncoder.encode(password));
        player.setRating(initial_rating); // set to initial rating when player is first created
        player.setAvailability(false);
        this.playerRepository.save(player);

        return player;
    }

    // Login: Check if the provided email and password are correct
    public Optional<Player> login(String email, String password) {
        Optional<Player> player = playerRepository.findByEmail(email);
        // Check if the player exists and if the password matches
        if (player.isPresent() && passwordEncoder.matches(password, player.get().getPassword())) {
            return player;
        }
        return Optional.empty();
    }

    // private String userName;

    // private String password;

    // @Column(unique = true)
    // private String email;

    // private float rating;

    // private boolean availability;
}
