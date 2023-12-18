package com.example.betteriter.fo_domain.user.exception;

import lombok.Getter;

@Getter
public class RefreshTokenIsNotValidException extends RuntimeException {
    public RefreshTokenIsNotValidException(String message) {
        super(message);
    }
}
