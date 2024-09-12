package com.ila.checkmatecentral.exceptions;

import org.springframework.security.core.AuthenticationException;

import com.ila.checkmatecentral.entity.UserAccount;

public class AccountExistsException extends AuthenticationException {

	public AccountExistsException(UserAccount user) {
		super("User already exists: " + user.getEmail());
	}
    
}
