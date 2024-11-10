package com.ila.checkmatecentral.controller;

import com.ila.checkmatecentral.entity.LoginRequest;
import com.ila.checkmatecentral.exceptions.InvalidCredentialsException;
import com.ila.checkmatecentral.utility.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.ila.checkmatecentral.entity.UserAccount;
import com.ila.checkmatecentral.service.UserAccountService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserAccountService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    /*
     * TODO: Move Exception Handling to Global Exception Handler
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, Model model, BindingResult bindingResults) {

        Authentication authenticationRequest =
            UsernamePasswordAuthenticationToken.unauthenticated(request.getEmail(), request.getPassword());

        try {
            Authentication authResponse = this.authenticationManager.authenticate(authenticationRequest);
            final UserDetails userDetails;
            final String jwtToken;
            if (authResponse.getPrincipal() instanceof UserDetails) {
                userDetails = (UserDetails) authResponse.getPrincipal();
                jwtToken = jwtUtil.generateToken(userDetails);

                Map<String, Object> response = new HashMap<>();
                response.put("token", jwtToken);
                response.put("expiry", jwtUtil.extractExpiration(jwtToken));
                response.put("username", userDetails.getUsername());

                return ResponseEntity.ok().body(response);
            }

        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        return null;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserAccount user) {
        // TODO: Refactor granted authority validation
        if(user.validAuthority()) {
            UserAccount createdUser =  userService.register(user);
            return new ResponseEntity<UserAccount>(createdUser, HttpStatus.CREATED);
        }
        return ResponseEntity.badRequest().body("Invalid authority: " + user.getGrantedAuthorityString());
    }
}
