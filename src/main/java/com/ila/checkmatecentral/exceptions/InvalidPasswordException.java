package com.ila.checkmatecentral.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * Class for exceptions related to passwords that do not conform to security requirements.
 * @author Jin
 */
public class InvalidPasswordException extends AuthenticationException {

	public InvalidPasswordException(String message) {
		super(message);
	}

}
