package com.ila.checkmatecentral.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ila.checkmatecentral.config.CurrentUser;
import com.ila.checkmatecentral.entity.UserAccount;
import com.ila.checkmatecentral.repository.UserAccountRepository;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserAccountRepository repository;
    
    public UserController(UserAccountRepository userService) {
        this.repository = userService;
    }

    @GetMapping
    public Map<String, ?> getUserSelf(@CurrentUser UserAccount user) {
        return Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail());
    }

    @GetMapping("/{id}")
    public Map<String, ?> getUser(@PathVariable("id") long id, @CurrentUser UserAccount user) {
        UserAccount targetUser = repository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "User ID not found: " + id));

        if (user.getId().equals(targetUser.getId())) {
            return Map.of(
                    "name", user.getName(),
                    "email", user.getEmail());
        } else {
            return Map.of("name", user.getName());
        }
    }
}
