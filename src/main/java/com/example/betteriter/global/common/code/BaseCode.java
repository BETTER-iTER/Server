package com.example.betteriter.global.common.code;

import com.example.betteriter.global.common.response.ResponseDto;

public interface BaseCode {

    public ResponseDto.ReasonDto getReason();

    public ResponseDto.ReasonDto getReasonHttpStatus();

}
