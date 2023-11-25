package com.example.betteriter.bo_domain.menufacturer.exception;

import com.example.betteriter.global.common.code.status.ErrorStatus;
import com.example.betteriter.global.common.exception.GeneralException;

public class ManufacturerHandler extends GeneralException {

        public ManufacturerHandler(ErrorStatus errorStatus) {
            super(errorStatus);
        }
}
