package com.example.betteriter.bo_domain.notification.exception;

import com.example.betteriter.global.common.code.BaseErrorCode;
import com.example.betteriter.global.common.exception.GeneralException;

public class FCMHandler extends GeneralException {

    public FCMHandler(BaseErrorCode code) {
        super(code);
    }

}
