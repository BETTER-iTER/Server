package com.example.betteriter.fo_domain.follow.exception;

import com.example.betteriter.global.error.exception.ErrorCode;
import com.example.betteriter.global.error.exception.GeneralException;

public class FollowHandler extends GeneralException {

        public FollowHandler(ErrorCode errorCode) {
            super(errorCode);
        }
}
