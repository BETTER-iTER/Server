package com.example.betteriter.bo_domain.notification.exception;

import com.example.betteriter.global.common.code.BaseErrorCode;
import com.example.betteriter.global.common.exception.GeneralException;

public class NotificationHandler extends GeneralException {

    public NotificationHandler(BaseErrorCode code) {
        super(code);
    }

}
