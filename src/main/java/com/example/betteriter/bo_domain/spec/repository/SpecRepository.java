package com.example.betteriter.bo_domain.spec.repository;

import com.example.betteriter.bo_domain.spec.domain.Spec;
import com.example.betteriter.global.constant.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecRepository extends JpaRepository<Spec, Long> {
    List<Spec> findAllByCategory(Category category);
}
