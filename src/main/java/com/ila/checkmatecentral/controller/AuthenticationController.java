package com.ila.checkmatecentral.controller;

import com.ila.checkmatecentral.entity.Admin;
import com.ila.checkmatecentral.entity.LoginRequest;
import com.ila.checkmatecentral.entity.Player;
import com.ila.checkmatecentral.service.AccountCredentialService;
import com.ila.checkmatecentral.utility.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
        Map<String, String> response = new HashMap<>();
        response.put("message", "Player created successfully.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody Map<String, String> map) {
        Admin admin = new Admin(map.get("email"), map.get("name"), map.get("password"));
        credentialService.registerAdmin(admin);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Admin created successfully.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        Authentication authenticationRequest =
            UsernamePasswordAuthenticationToken.unauthenticated(request.getEmail(), request.getPassword());

            Authentication authResponse = authenticationManager.authenticate(authenticationRequest);
            if (authResponse.getPrincipal() instanceof UserDetails userDetails) {
                final String jwtToken = JwtUtil.generateToken(userDetails);

                Map<String, Object> response = new HashMap<>();
                response.put("token", jwtToken);
                response.put("expiry", JwtUtil.extractExpiration(jwtToken));
                response.put("username", userDetails.getUsername());

                return ResponseEntity.ok().body(response);
            }
        return null;
    }
}
