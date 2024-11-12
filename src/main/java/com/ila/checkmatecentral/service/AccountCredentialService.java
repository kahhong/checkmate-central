package com.ila.checkmatecentral.service;

import com.ila.checkmatecentral.entity.AccountCredential;
import com.ila.checkmatecentral.entity.Admin;
import com.ila.checkmatecentral.entity.Player;
import com.ila.checkmatecentral.exceptions.AccountExistsException;
import com.ila.checkmatecentral.exceptions.InvalidCredentialsException;
import com.ila.checkmatecentral.repository.AdminRepository;
import com.ila.checkmatecentral.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountCredentialService implements UserDetailsService {
    private final PlayerRepository playerRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Although the method name is load by username, we identify users by email here.
     * @param email the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    public AccountCredential loadUserByUsername(String email) throws UsernameNotFoundException {
        return playerRepository.findByEmail(email)
            .orElseGet(() -> adminRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found: " + email))
        );
    }

    public Player loadPlayerById(Long id) throws AuthenticationCredentialsNotFoundException {
        return playerRepository.findById(id)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Id not found: " + id));
    }

    public Admin loadAdminById(Long id) throws AuthenticationCredentialsNotFoundException {
        return adminRepository.findById(id)
            .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Id not found: " + id));
    }

    public Player registerPlayer(Player player) throws AccountExistsException {
        if(!exists(player)) {
            encodePassword(player);
            return playerRepository.save(player);

        } else {
            throw new AccountExistsException(player);
        }
    }

    public Admin registerAdmin(Admin admin) throws AccountExistsException {
        if(!exists(admin)) {
            encodePassword(admin);
            return adminRepository.save(admin);

        } else {
            throw new AccountExistsException(admin);
        }
    }






    private boolean exists(AccountCredential credential) {
        return playerRepository.findByEmail(credential.getEmail())
            .or(() -> adminRepository.findByEmail(credential.getEmail()))
            .isPresent();
    }

    private void encodePassword(AccountCredential credential) {
        String rawPassword = credential.getPassword();
        validatePassword(rawPassword);

        String encodedPassword = passwordEncoder.encode(rawPassword);
        credential.setPassword(encodedPassword);
    }

    private static void validatePassword(String rawPassword) throws InvalidCredentialsException {
        final int MIN_LENGTH = 8;
        final int MAX_LENGTH = 30;

        if (rawPassword.length() < MIN_LENGTH || rawPassword.length() > MAX_LENGTH) {
            throw new InvalidCredentialsException(
                "Password must be between %d and %d characters."
                    .formatted(MIN_LENGTH, MAX_LENGTH));
        }
    }
}
