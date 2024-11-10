package com.ila.checkmatecentral.repository;

import com.ila.checkmatecentral.entity.AdminAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface AdminAccountRepository extends CrudRepository<AdminAccount, Long> {
    @NonNull
    Optional<AdminAccount> findById(@NonNull Long id);

    Optional<AdminAccount> findByEmail(String email);
}
