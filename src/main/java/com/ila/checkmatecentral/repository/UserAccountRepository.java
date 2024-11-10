package com.ila.checkmatecentral.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.ila.checkmatecentral.entity.UserAccount;
import org.springframework.lang.NonNull;

public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {
    @NonNull
    Optional<UserAccount> findById(@NonNull Long id);

    Optional<UserAccount> findByEmail(String email);
}
