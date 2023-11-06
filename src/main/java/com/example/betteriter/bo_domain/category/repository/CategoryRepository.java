package com.example.betteriter.bo_domain.category.repository;

import com.example.betteriter.bo_domain.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
