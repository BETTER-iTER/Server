package com.example.betteriter.global.common.code.status;

import com.example.betteriter.global.common.code.BaseCode;
import com.example.betteriter.global.common.exception.GeneralException;
import com.example.betteriter.global.common.response.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum SuccesStatus implements BaseCode {

    // Success
    _OK(HttpStatus.OK, "SUCCESS_200", "OK"),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ApiResponse.ReasonDto getReason() {
        return ApiResponse.ReasonDto.builder()
                .isSuccess(true)
                .code(this.code)
                .message(this.message)
                .build();
    }

    @Override
    public ApiResponse.ReasonDto getReasonHttpStatus() {
        return ApiResponse.ReasonDto.builder()
                .httpStatus(this.httpStatus)
                .isSuccess(true)
                .code(this.code)
                .message(this.message)
                .build();
    }
}
