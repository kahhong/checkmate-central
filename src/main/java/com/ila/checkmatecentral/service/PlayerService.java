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

    public void updateAvailability(Player player, String availabilityText){
        if(!availabilityText.equals("True")||!availabilityText.equals("True")){
            throw new InvalidAvailabilityException("Only accept True or False for availability");
        }
        if(availabilityText.equals("True")){
            player.setAvailability(true);
        }else if (availabilityText.equals("False")) {
            player.setAvailability(false);
        }
        playerRepository.save(player);
    }

    public boolean getAvailability(Player player){
        return player.isAvailability();
    }
}
