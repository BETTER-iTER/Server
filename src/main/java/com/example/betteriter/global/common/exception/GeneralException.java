package com.example.betteriter.global.common.exception;

import com.example.betteriter.global.common.code.BaseErrorCode;
import com.example.betteriter.global.common.code.status.ErrorStatus;
import com.example.betteriter.global.common.response.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private final BaseErrorCode code;

    public ApiResponse.ErrorReasonDto getErrorReason() {
        return this.code.getReason();
    }

    public ApiResponse.ErrorReasonDto getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}
