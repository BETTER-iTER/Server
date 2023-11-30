package com.example.betteriter.bo_domain.spec.service;

import com.example.betteriter.bo_domain.spec.domain.Spec;
import com.example.betteriter.bo_domain.spec.domain.SpecData;
import com.example.betteriter.bo_domain.spec.repository.SpecDataRepository;
import com.example.betteriter.bo_domain.spec.repository.SpecRepository;
import com.example.betteriter.global.constant.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SpecService {
    private final SpecRepository specRepository;
    private final SpecDataRepository specDataRepository;

    public List<SpecData> findAllSpecDataByIds(List<Long> id) {
        return this.specDataRepository.findAllByIdIn(id);
    }

    public List<Spec> findAllSpecDataByCategory(Category category) {
        return this.specRepository.findAllByCategory(category);
    }
}
