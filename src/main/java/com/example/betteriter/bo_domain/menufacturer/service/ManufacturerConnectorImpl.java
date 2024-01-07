package com.example.betteriter.bo_domain.menufacturer.service;

import com.example.betteriter.bo_domain.menufacturer.domain.Manufacturer;
import com.example.betteriter.bo_domain.menufacturer.repository.ManufacturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ManufacturerConnectorImpl implements ManufacturerConnector {
    private final ManufacturerRepository manufacturerRepository;

    @Override
    public Manufacturer findManufacturerByName(String name) {
        Optional<Manufacturer> optionalManufacturer = this.manufacturerRepository.findByCoName(name);
        return optionalManufacturer.orElseGet(() -> this.manufacturerRepository.save(Manufacturer.createManufacturer(name)));
    }
}
