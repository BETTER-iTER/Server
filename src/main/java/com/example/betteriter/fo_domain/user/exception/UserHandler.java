package com.example.betteriter.fo_domain.user.exception;

import com.example.betteriter.global.common.code.status.ErrorStatus;
import com.example.betteriter.global.common.exception.GeneralException;

public class UserHandler extends GeneralException {
    public UserHandler(ErrorStatus errorStatus) {
        super(errorStatus);
    }
}
