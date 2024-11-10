package com.ila.checkmatecentral.controller;

import com.ila.checkmatecentral.entity.AdminAccount;
import com.ila.checkmatecentral.entity.LoginRequest;
import com.ila.checkmatecentral.exceptions.InvalidCredentialsException;
import com.ila.checkmatecentral.service.AdminAccountService;
import com.ila.checkmatecentral.utility.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/admin")
@Slf4j
@RequiredArgsConstructor
public class AdminAuthenticationController {
    private final AdminAccountService adminService;
    @Resource(name = "adminAuthenticationManager")
    private final AuthenticationManager adminAuthenticationManager;
    private final JwtUtil jwtUtil;

    /*
     * TODO: Move Exception Handling to Global Exception Handler
     */
    @PostMapping("/register")
    public ResponseEntity<?> adminRegister(@RequestBody AdminAccount admin) {
        // TODO: Refactor granted authority validation
        adminService.register(admin);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Admin account registered successfully.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@Valid @RequestBody LoginRequest request) {

        Authentication authenticationRequest =
            UsernamePasswordAuthenticationToken.unauthenticated(request.getEmail(), request.getPassword());

        try {
            Authentication authResponse = this.adminAuthenticationManager.authenticate(authenticationRequest);
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
}
