package com.example.betteriter.bo_domain.example.exception;

import com.example.betteriter.global.common.code.status.ErrorStatus;
import com.example.betteriter.global.common.exception.GeneralException;

public class TempHandler extends GeneralException {

    public TempHandler(ErrorStatus errorStatus) {
        super(errorStatus);
    }

}
