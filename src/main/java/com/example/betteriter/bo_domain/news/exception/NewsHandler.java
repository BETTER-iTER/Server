package com.example.betteriter.bo_domain.news.exception;

import com.example.betteriter.global.common.code.status.ErrorStatus;
import com.example.betteriter.global.common.exception.GeneralException;

public class NewsHandler extends GeneralException {

        public NewsHandler(ErrorStatus errorStatus) {
            super(errorStatus);
        }
}
