package com.ila.checkmatecentral.repository;

import com.ila.checkmatecentral.entity.AccountCredential;
import com.ila.checkmatecentral.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    @NonNull
    Optional<Admin> findById(@NonNull Long id);
    Optional<AccountCredential> findByEmail(String email);
}
