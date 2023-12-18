package com.example.betteriter.bo_domain.example.service;

import com.example.betteriter.bo_domain.example.exception.TempHandler;
import com.example.betteriter.global.common.code.status.ErrorStatus;
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
            throw new TempHandler(ErrorStatus.TEST_BAD_REQUEST);
        }
    }
}

