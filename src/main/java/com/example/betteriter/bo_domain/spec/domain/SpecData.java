package com.example.betteriter.bo_domain.spec.domain;


import com.example.betteriter.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "SPEC_DATA")
public class SpecData extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "spec_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Spec spec;

    @Column(name = "spec_data")
    private String data;

    @Builder
    private SpecData(Spec spec, String data) {
        this.spec = spec;
        this.data = data;
    }
}
