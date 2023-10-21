package com.example.betteriter.fo_domain.review.exception;

import com.example.betteriter.global.error.exception.ErrorCode;
import com.example.betteriter.global.error.exception.GeneralException;

public class ReviewHandler extends GeneralException {

        public ReviewHandler(ErrorCode errorCode) {
            super(errorCode);
        }
}
