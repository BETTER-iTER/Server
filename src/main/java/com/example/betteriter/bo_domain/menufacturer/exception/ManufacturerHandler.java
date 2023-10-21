package com.example.betteriter.bo_domain.menufacturer.exception;

import com.example.betteriter.global.error.exception.ErrorCode;
import com.example.betteriter.global.error.exception.GeneralException;

public class ManufacturerHandler extends GeneralException {

        public ManufacturerHandler(ErrorCode errorCode) {
            super(errorCode);
        }
}
