package com.example.betteriter.global.common.code;

import com.example.betteriter.global.common.response.ResponseDto;

public interface BaseErrorCode {

    public ResponseDto.ErrorReasonDto getReason();

    public ResponseDto.ErrorReasonDto getReasonHttpStatus();

}
