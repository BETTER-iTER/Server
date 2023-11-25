package com.example.betteriter.global.common.code.status;

import com.example.betteriter.global.common.code.BaseCode;
import com.example.betteriter.global.common.response.ResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

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
    public ResponseDto.ReasonDto getReason() {
        return ResponseDto.ReasonDto.builder()
                .isSuccess(true)
                .code(this.code)
                .message(this.message)
                .build();
    }

    @Override
    public ResponseDto.ReasonDto getReasonHttpStatus() {
        return ResponseDto.ReasonDto.builder()
                .httpStatus(this.httpStatus)
                .isSuccess(true)
                .code(this.code)
                .message(this.message)
                .build();
    }
}
