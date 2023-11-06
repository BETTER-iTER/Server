package com.example.betteriter.bo_domain.home.controller;

import com.example.betteriter.bo_domain.home.dto.GetHomeResponseDto;
import com.example.betteriter.bo_domain.home.service.HomeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class HomeController {
    private final HomeService homeService;

    @GetMapping("/home")
    public ResponseEntity<GetHomeResponseDto> getHome() {
        return new ResponseEntity<>(this.homeService.getHome(), HttpStatus.OK);
    }
}
