package com.example.betteriter.fo_domain.user.exception;

import com.example.betteriter.global.error.exception.ErrorCode;
import com.example.betteriter.global.error.exception.GeneralException;

public class UserHandler extends GeneralException {
    public UserHandler(ErrorCode errorCode) {
        super(errorCode);
    }
}
