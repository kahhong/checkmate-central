package com.ila.checkmatecentral.service;

import com.ila.checkmatecentral.entity.AdminAccount;
import com.ila.checkmatecentral.exceptions.AccountExistsException;
import com.ila.checkmatecentral.exceptions.InvalidCredentialsException;
import com.ila.checkmatecentral.repository.AdminAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminAccountService implements UserDetailsService {
    private final AdminAccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AdminAccountService(AdminAccountRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AdminAccount loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    public AdminAccount loadUserById(long id) throws UsernameNotFoundException {
        return repository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("" + id));
    }
    
    public boolean exists(String email) {
        return repository.findByEmail(email).isPresent();
    }
    
    public AdminAccount register(AdminAccount user) throws AuthenticationException {
        if(exists(user.getEmail())) {
            throw new AccountExistsException(user);
        }
        
        String rawPassword = user.getPassword();
        validatePassword(rawPassword);
        
        log.info("Registering: %s | %s | %s", user.getName(), user.getEmail(), user.getPassword());

        String password = passwordEncoder.encode(rawPassword);
        
        return repository.save(new AdminAccount(user.getEmail(), user.getName(), password));
    }
    
    // TODO: Externalize password validation into another class for more complex requirements
    private void validatePassword(String rawPassword) throws InvalidCredentialsException {
        final int MIN_LENGTH = 8;
        final int MAX_LENGTH = 30;

        if (rawPassword == null || rawPassword.length() < MIN_LENGTH || rawPassword.length() > MAX_LENGTH) {
            throw new InvalidCredentialsException(
                "Password must be between %d and %d characters."
                .formatted(MIN_LENGTH, MAX_LENGTH));
        }
    }
}
