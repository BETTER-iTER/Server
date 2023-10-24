package com.example.betteriter.bo_domain.news.exception;

import com.example.betteriter.global.error.exception.ErrorCode;
import com.example.betteriter.global.error.exception.GeneralException;

public class NewsHandler extends GeneralException {

        public NewsHandler(ErrorCode errorCode) {
            super(errorCode);
        }
}
