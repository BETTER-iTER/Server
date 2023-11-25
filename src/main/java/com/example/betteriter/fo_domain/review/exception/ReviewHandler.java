package com.example.betteriter.fo_domain.review.exception;

import com.example.betteriter.global.common.code.status.ErrorStatus;
import com.example.betteriter.global.common.exception.GeneralException;

public class ReviewHandler extends GeneralException {

        public ReviewHandler(ErrorStatus errorStatus) {
            super(errorStatus);
        }
}
