package com.example.betteriter.bo_domain.spec.service;

import com.example.betteriter.bo_domain.spec.domain.Spec;
import com.example.betteriter.bo_domain.spec.domain.SpecData;
import com.example.betteriter.bo_domain.spec.repository.SpecDataRepository;
import com.example.betteriter.bo_domain.spec.repository.SpecRepository;
import com.example.betteriter.global.constant.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SpecConnectorImpl implements SpecConnector {

    private final SpecRepository specRepository;
    private final SpecDataRepository specDataRepository;

    @Override
    public List<SpecData> findAllSpecDataByIds(List<Long> id) {
        return this.specDataRepository.findAllByIdIn(id);
    }

    @Override
    public List<Spec> findAllSpecDataByCategory(Category category) {
        return this.specRepository.findAllByCategory(category);
    }
}
