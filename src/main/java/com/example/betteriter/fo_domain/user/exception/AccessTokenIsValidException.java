package com.example.betteriter.fo_domain.user.exception;

import org.springframework.security.core.AuthenticationException;

public class AccessTokenIsValidException extends AuthenticationException {
    private final static String message = "AccessTokenIsValidException";

    public AccessTokenIsValidException() {
        super(message);
    }
}
