package com.ila.checkmatecentral.exceptions;

import com.ila.checkmatecentral.entity.AccountCredential;
import org.springframework.security.core.AuthenticationException;

public class AccountExistsException extends AuthenticationException {

	public AccountExistsException(AccountCredential credential) {
		super("User already exists: " + credential.getEmail());
	}
}
