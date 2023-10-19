package com.example.betteriter.bo_domain.category.exception;

import com.example.betteriter.global.error.exception.ErrorCode;
import com.example.betteriter.global.error.exception.GeneralException;

public class CategoryHandler extends GeneralException {

        public CategoryHandler(ErrorCode errorCode) {
            super(errorCode);
        }
}
