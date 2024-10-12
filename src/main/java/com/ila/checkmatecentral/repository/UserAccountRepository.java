package com.ila.checkmatecentral.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.ila.checkmatecentral.entity.UserAccount;

public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {
    Optional<UserAccount> findByEmail(String email);
}
