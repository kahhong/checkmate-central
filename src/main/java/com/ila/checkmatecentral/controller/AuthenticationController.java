package com.ila.checkmatecentral.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ila.checkmatecentral.entity.AccountCredential;
import com.ila.checkmatecentral.entity.Admin;
import com.ila.checkmatecentral.entity.LoginRequest;
import com.ila.checkmatecentral.entity.Player;
import com.ila.checkmatecentral.service.AccountCredentialService;
import com.ila.checkmatecentral.utility.JwtUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {
    private final AccountCredentialService credentialService;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/register/player")
    public ResponseEntity<?> registerPlayer(@RequestBody Map<String, String> map) {
        Player player = new Player(map.get("email"), map.get("name"), map.get("password"));
        credentialService.registerPlayer(player);
        Map<String, String> response = Map.of("message", "Player created successfully.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody Map<String, String> map) {
        Admin admin = new Admin(map.get("email"), map.get("name"), map.get("password"));
        credentialService.registerAdmin(admin);
        Map<String, String> response = Map.of("message", "Admin created successfully.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        Authentication authenticationRequest =
            UsernamePasswordAuthenticationToken.unauthenticated(request.getEmail(), request.getPassword());

        Authentication authResponse = authenticationManager.authenticate(authenticationRequest);
        if (authResponse.getPrincipal() instanceof UserDetails userDetails) {
            final String jwtToken = JwtUtil.generateToken(userDetails);

            AccountCredential credential = credentialService.loadUserByUsername(request.getEmail());

            Map<String, Object> response = Map.of(
                    "id", credential.getId(),
                    "token", jwtToken,
                    "expiry", JwtUtil.extractExpiration(jwtToken),
                    "username", userDetails.getUsername()
            );

            return ResponseEntity.ok().body(response);
        }
        return null;
    }
}
