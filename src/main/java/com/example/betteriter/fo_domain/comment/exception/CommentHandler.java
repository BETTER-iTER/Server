package com.example.betteriter.fo_domain.comment.exception;

import com.example.betteriter.global.error.exception.ErrorCode;
import com.example.betteriter.global.error.exception.GeneralException;

public class CommentHandler extends GeneralException {

        public CommentHandler(ErrorCode errorCode) {
            super(errorCode);
        }
}
