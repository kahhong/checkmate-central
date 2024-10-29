package com.ila.checkmatecentral.controller;

import org.springframework.web.bind.annotation.RestController;

import com.ila.checkmatecentral.entity.LoginRequest;
import com.ila.checkmatecentral.entity.Match;
import com.ila.checkmatecentral.entity.UserAccount;
import com.ila.checkmatecentral.exceptions.TournamentNotFoundException;
import com.ila.checkmatecentral.service.UserAccountService;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;


@RequestMapping("/api/profile")
@RequiredArgsConstructor
@RestController
public class UserProfileController {
    private final UserAccountService userAccountService; 

    @GetMapping({"/user/{email}"})
    public ResponseEntity<?> getMatch(@PathVariable("email") String email) {
        try {
            UserAccount user = userAccountService.loadUserByUsername(email); 
            return ResponseEntity.status(HttpStatus.OK).body(
                Map.of(
                    "name", user.getName(), 
                    "email", user.getEmail(), 
                    "rating", user.getRating(),
                    "timeLastPlayed", user.getTimeLastPlayed()
                )
            );
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
        }
    }
}




