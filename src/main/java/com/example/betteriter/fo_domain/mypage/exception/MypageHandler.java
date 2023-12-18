package com.example.betteriter.fo_domain.mypage.exception;

import com.example.betteriter.global.common.code.status.ErrorStatus;
import com.example.betteriter.global.common.exception.GeneralException;

public class MypageHandler extends GeneralException {

        public MypageHandler(ErrorStatus errorStatus) {
            super(errorStatus);
        }
}
