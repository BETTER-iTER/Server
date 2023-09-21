package com.example.betteriter.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private int code;
    private String errorSimpleName;
    private String msg;
    private LocalDateTime timestamp;

    public ErrorResponse(Exception exception, HttpStatus httpStatus) {
        this.code = httpStatus.value();
        this.errorSimpleName = exception.getClass().getSimpleName();
        this.msg = exception.getLocalizedMessage();
        this.timestamp = LocalDateTime.now();
    }

    public static ErrorResponse of(Exception exception, HttpStatus httpStatus) {
        return new ErrorResponse(exception, httpStatus);
    }
}
