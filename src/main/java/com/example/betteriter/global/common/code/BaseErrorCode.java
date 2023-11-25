package com.example.betteriter.global.common.code;

import com.example.betteriter.global.common.response.ApiResponse;

public interface BaseErrorCode {

    public ApiResponse.ErrorReasonDto getReason();

    public ApiResponse.ErrorReasonDto getReasonHttpStatus();

}
