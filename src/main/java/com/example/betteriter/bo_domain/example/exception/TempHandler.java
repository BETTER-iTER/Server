package com.example.betteriter.bo_domain.example.exception;

import com.example.betteriter.global.error.exception.ErrorCode;
import com.example.betteriter.global.error.exception.GeneralException;

public class TempHandler extends GeneralException {

    public TempHandler(ErrorCode errorCode) {
        super(errorCode);
    }

}