package com.example.betteriter.global.common.code;

import com.example.betteriter.global.common.response.ApiResponse;

public interface BaseCode {

    public ApiResponse.ReasonDto getReason();

    public ApiResponse.ReasonDto getReasonHttpStatus();

}
