package com.example.betteriter.global.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@Slf4j
@RestControllerAdvice(basePackages = "com.example.betteriter")
public class GlobalExceptionHandler {
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> ioExceptionHandler(
            IOException exception
    ) {
        return ResponseEntity.internalServerError()
                .body(ErrorResponse.of(exception, HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
