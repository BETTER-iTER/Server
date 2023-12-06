package com.example.betteriter.bo_domain.menufacturer.service;

import com.example.betteriter.bo_domain.menufacturer.domain.Manufacturer;
import com.example.betteriter.bo_domain.menufacturer.repository.ManufacturerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;

    public Manufacturer findManufacturerByName(String name) {
        return this.manufacturerRepository.findByCoName(name)
                .orElseGet(() -> this.manufacturerRepository.save(Manufacturer.builder().coName(name).build()));
    }
}
