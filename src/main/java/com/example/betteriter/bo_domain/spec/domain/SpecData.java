package com.example.betteriter.bo_domain.spec.domain;


import com.example.betteriter.bo_domain.category.domain.Category;
import com.example.betteriter.global.common.entity.BaseEntity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Getter
@Builder
@AllArgsConstructor
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
}
