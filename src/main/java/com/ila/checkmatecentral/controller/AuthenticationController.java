package com.ila.checkmatecentral.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ila.checkmatecentral.entity.UserAccount;
import com.ila.checkmatecentral.service.UserAccountService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserAccountService userService;

	public AuthenticationController(UserAccountService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserAccount user) {
        userService.register(user);
        return ResponseEntity.ok("User registered: " + user.getEmail());
    }
    
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleUserExistsException(AuthenticationException ex) {
        return ResponseEntity.ok(ex.getMessage());
    }
}
