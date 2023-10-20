package com.example.betteriter.bo_domain.spec.exception;

import com.example.betteriter.global.error.exception.ErrorCode;
import com.example.betteriter.global.error.exception.GeneralException;

public class SpecHandler extends GeneralException {

        public SpecHandler(ErrorCode errorCode) {
            super(errorCode);
        }
}
