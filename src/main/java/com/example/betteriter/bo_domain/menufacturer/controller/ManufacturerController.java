package com.example.betteriter.bo_domain.menufacturer.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ManufacturerController", description = "Manufacturer API")
@Slf4j
@RequestMapping("/manufacturer")
@RequiredArgsConstructor
@RestController
public class ManufacturerController {
}
