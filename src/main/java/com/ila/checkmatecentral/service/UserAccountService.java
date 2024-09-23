package com.ila.checkmatecentral.service;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ila.checkmatecentral.entity.UserAccount;
import com.ila.checkmatecentral.exceptions.InvalidPasswordException;
import com.ila.checkmatecentral.exceptions.AccountExistsException;
import com.ila.checkmatecentral.repository.UserAccountRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserAccountService implements UserDetailsService {
    private final UserAccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserAccountService(UserAccountRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserAccount loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    public UserAccount loadUserById(long id) throws UsernameNotFoundException {
        return repository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("" + id));
    }
    
    public boolean exists(String email) {
        return repository.findByEmail(email).isPresent();
    }
    
    public UserAccount register(UserAccount user) throws AuthenticationException {
        
        if (exists(user.getEmail())) {
            throw new AccountExistsException(user);
        }
        
        String rawPassword = user.getPassword();
        validatePassword(rawPassword);
        
        log.info("Registering: %s | %s | %s", user.getName(), user.getEmail(), user.getPassword());

        String password = passwordEncoder.encode(rawPassword);
        
        return repository.save(new UserAccount(user.getEmail(), user.getName(), password));
    }
    
    // TODO: Externalize password validation into another class for more complex requirements
    private void validatePassword(String rawPassword) throws InvalidPasswordException {
        final int MIN_LENGTH = 8;
        final int MAX_LENGTH = 30;

        if (rawPassword == null || rawPassword.length() < MIN_LENGTH || rawPassword.length() > MAX_LENGTH) {
            throw new InvalidPasswordException(
                "Password must be between %d and %d characters."
                .formatted(MIN_LENGTH, MAX_LENGTH));
        }
    }
}
