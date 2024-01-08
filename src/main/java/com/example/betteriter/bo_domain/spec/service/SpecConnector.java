package com.example.betteriter.bo_domain.spec.service;

import com.example.betteriter.bo_domain.spec.domain.Spec;
import com.example.betteriter.bo_domain.spec.domain.SpecData;
import com.example.betteriter.global.constant.Category;

import java.util.List;

public interface SpecConnector {
    List<SpecData> findAllSpecDataByIds(List<Long> id);

    List<Spec> findAllSpecDataByCategory(Category category);
}
