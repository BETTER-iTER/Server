package com.example.betteriter.bo_domain.spec.exception;

import com.example.betteriter.global.common.code.status.ErrorStatus;
import com.example.betteriter.global.common.exception.GeneralException;

public class SpecHandler extends GeneralException {

        public SpecHandler(ErrorStatus errorStatus) {
            super(errorStatus);
        }
}
