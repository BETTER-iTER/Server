package com.example.betteriter.bo_domain.menufacturer.service;

import com.example.betteriter.bo_domain.menufacturer.domain.Manufacturer;
import com.example.betteriter.bo_domain.menufacturer.exception.ManufacturerHandler;
import com.example.betteriter.bo_domain.menufacturer.repository.ManufacturerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.example.betteriter.global.common.code.status.ErrorStatus.MANUFACTURER_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;

    public Manufacturer findManufacturerById(Long id) {
        return this.manufacturerRepository.findById(id)
                .orElseThrow(() -> new ManufacturerHandler(MANUFACTURER_NOT_FOUND));
    }
}
