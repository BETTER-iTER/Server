package com.example.betteriter.bo_domain.menufacturer.service;

import com.example.betteriter.bo_domain.menufacturer.domain.Manufacturer;
import com.example.betteriter.bo_domain.menufacturer.repository.ManufacturerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;

    public Manufacturer findManufacturerByName(String name) {
        Optional<Manufacturer> optionalManufacturer = this.manufacturerRepository.findByCoName(name);
        if (optionalManufacturer.isEmpty()) {
            return this.manufacturerRepository.save(Manufacturer.createManufacturer(name));
        }
        return optionalManufacturer.get();
    }
}
