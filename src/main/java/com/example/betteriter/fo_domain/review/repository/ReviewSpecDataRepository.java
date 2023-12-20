package com.example.betteriter.fo_domain.review.repository;

import com.example.betteriter.bo_domain.spec.domain.SpecData;
import com.example.betteriter.fo_domain.review.domain.ReviewSpecData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewSpecDataRepository extends JpaRepository<ReviewSpecData, Long> {
    List<ReviewSpecData> findAllBySpecData(SpecData specData);
}
