package com.example.betteriter.bo_domain.example.controller;

import com.example.betteriter.bo_domain.example.dto.TempConverter;
import com.example.betteriter.bo_domain.example.dto.TempResponse;
import com.example.betteriter.bo_domain.example.service.TempQueryService;
import com.example.betteriter.global.common.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/temp")
@RequiredArgsConstructor
@RestController
public class TempController {

    private final TempQueryService tempQueryService;

    @GetMapping("/test")
    public ResponseDto<TempResponse.TempTestDto> test() {
        return ResponseDto.onSuccess(TempConverter.toTempTestDto());
    }

    @GetMapping("/exception")
    public ResponseDto<TempResponse.TempExceptionDto> exception(@RequestParam Integer flag) {
        tempQueryService.checkFlag(flag);
        return ResponseDto.onSuccess(TempConverter.toTempExceptionDto(flag));
    }
}
