package com.example.betteriter.fo_domain.follow.exception;

import com.example.betteriter.global.common.code.status.ErrorStatus;
import com.example.betteriter.global.common.exception.GeneralException;

public class FollowHandler extends GeneralException {

        public FollowHandler(ErrorStatus errorStatus) {
            super(errorStatus);
        }
}
