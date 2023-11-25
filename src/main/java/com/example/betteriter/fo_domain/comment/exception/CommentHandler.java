package com.example.betteriter.fo_domain.comment.exception;

import com.example.betteriter.global.common.code.status.ErrorStatus;
import com.example.betteriter.global.common.exception.GeneralException;

public class CommentHandler extends GeneralException {

        public CommentHandler(ErrorStatus errorStatus) {
            super(errorStatus);
        }
}
