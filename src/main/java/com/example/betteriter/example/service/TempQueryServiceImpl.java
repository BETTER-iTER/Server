package com.example.betteriter.example.service;

import com.example.betteriter.example.exception.TempHandler;
import com.example.betteriter.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TempQueryServiceImpl implements TempQueryService {

    @Override
    public void checkFlag(Integer flag) {
        if (flag == 3) {
            throw new TempHandler(ErrorCode.TEST_BAD_REQUEST);
        }
    }
}
