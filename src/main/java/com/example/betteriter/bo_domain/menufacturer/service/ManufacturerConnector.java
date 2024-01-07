package com.example.betteriter.bo_domain.menufacturer.service;

import com.example.betteriter.bo_domain.menufacturer.domain.Manufacturer;

public interface ManufacturerConnector {
    Manufacturer findManufacturerByName(String name);
}
