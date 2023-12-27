package com.example.betteriter.fo_domain.user.exception;

import lombok.Getter;

@Getter
public class RefreshTokenIsNotMatchException extends RuntimeException {
    private final static String message = "RefreshTokenIsNotMatchException";

    public RefreshTokenIsNotMatchException() {
        super(message);
    }
}
