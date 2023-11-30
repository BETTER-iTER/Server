package com.example.betteriter.bo_domain.spec.repository;

import com.example.betteriter.bo_domain.spec.domain.SpecData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecDataRepository extends JpaRepository<SpecData, Long> {
    List<SpecData> findAllByIdIn(List<Long> id);
}
