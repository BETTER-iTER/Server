package com.example.betteriter.bo_domain.announce.exception;

import com.example.betteriter.global.common.code.status.ErrorStatus;
import com.example.betteriter.global.common.exception.GeneralException;

public class AnnounceHandler extends GeneralException {

        public AnnounceHandler(ErrorStatus errorStatus) {
            super(errorStatus);
        }
}
