package com.example.betteriter.fo_domain.search.exception;

import com.example.betteriter.global.error.exception.ErrorCode;
import com.example.betteriter.global.error.exception.GeneralException;

public class SearchHandler extends GeneralException {

        public SearchHandler(ErrorCode errorCode) {
            super(errorCode);
        }
}
