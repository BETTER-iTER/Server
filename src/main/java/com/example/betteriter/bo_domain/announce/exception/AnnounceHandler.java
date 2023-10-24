package com.example.betteriter.bo_domain.announce.exception;

import com.example.betteriter.global.error.exception.ErrorCode;
import com.example.betteriter.global.error.exception.GeneralException;

public class AnnounceHandler extends GeneralException {

        public AnnounceHandler(ErrorCode errorCode) {
            super(errorCode);
        }
}
