package com.ila.checkmatecentral.service;


import com.ila.checkmatecentral.entity.Player;
import com.ila.checkmatecentral.exceptions.InvalidAvailabilityException;
import com.ila.checkmatecentral.exceptions.PlayerNotFoundException;

import com.ila.checkmatecentral.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class PlayerService {
    public final PlayerRepository playerRepository;

    public Player getPlayer(Long playerId) {
        return playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException(playerId));
    }

    public void updateProfileDescription(Player player, String description){
        player.setProfileDescription(description);
        playerRepository.save(player);
    }

    public Player updateAvailability(Player player, String availabilityText){
        if (!("true".equalsIgnoreCase(availabilityText) || "false".equalsIgnoreCase(availabilityText)))
            throw new InvalidAvailabilityException("Only accept True or False for availability");

        boolean availability = Boolean.parseBoolean(availabilityText);
        player.setAvailability(availability);
        return playerRepository.save(player);
    }

    public long getNumberOfPlayersWithHigherRating(Player player) {
        return playerRepository.countByRatingGreaterThan(player.getRating());
    }
}
