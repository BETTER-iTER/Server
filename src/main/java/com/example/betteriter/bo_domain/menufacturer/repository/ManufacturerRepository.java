package com.example.betteriter.bo_domain.menufacturer.repository;

import com.example.betteriter.bo_domain.menufacturer.domain.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
    Optional<Manufacturer> findByCoName(String coName);
}
