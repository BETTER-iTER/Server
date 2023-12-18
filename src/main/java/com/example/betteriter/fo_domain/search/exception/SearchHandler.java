package com.example.betteriter.fo_domain.search.exception;

import com.example.betteriter.global.common.code.status.ErrorStatus;
import com.example.betteriter.global.common.exception.GeneralException;

public class SearchHandler extends GeneralException {

        public SearchHandler(ErrorStatus errorStatus) {
            super(errorStatus);
        }
}
