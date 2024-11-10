package com.ila.checkmatecentral.exceptions;

import com.ila.checkmatecentral.entity.AdminAccount;
import org.springframework.security.core.AuthenticationException;

import com.ila.checkmatecentral.entity.UserAccount;

public class AccountExistsException extends AuthenticationException {

	public AccountExistsException(UserAccount user) {
		super("User already exists: " + user.getEmail());
	}

	public AccountExistsException(AdminAccount user) {
		super("User already exists: " + user.getEmail());
	}
}
