package com.example.betteriter.bo_domain.quiz.exception;

import com.example.betteriter.global.error.exception.ErrorCode;
import com.example.betteriter.global.error.exception.GeneralException;

public class QuizHandler extends GeneralException {

        public QuizHandler(ErrorCode errorCode) {
            super(errorCode);
        }
}
