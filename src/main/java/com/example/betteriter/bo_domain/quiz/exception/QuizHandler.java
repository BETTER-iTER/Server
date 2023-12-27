package com.example.betteriter.bo_domain.quiz.exception;

import com.example.betteriter.global.common.code.status.ErrorStatus;
import com.example.betteriter.global.common.exception.GeneralException;

public class QuizHandler extends GeneralException {

        public QuizHandler(ErrorStatus errorStatus) {
            super(errorStatus);
        }
}
