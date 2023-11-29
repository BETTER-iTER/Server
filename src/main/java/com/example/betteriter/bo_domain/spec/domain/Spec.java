package com.example.betteriter.bo_domain.spec.domain;


import com.example.betteriter.global.common.entity.BaseEntity;
import com.example.betteriter.global.constant.Category;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "SPEC")
public class Spec extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category; // 어떤 카테고리에 대한 스펙인지

    @Column(name = "spec_title")
    private String title;

    @OneToMany(mappedBy = "spec", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SpecData> specData;

    @Builder
    private Spec(Category category, String title, List<SpecData> specData) {
        this.category = category;
        this.title = title;
        this.specData = specData;
    }

    public static Spec CreateSpec(Category category, String title, List<SpecData> specData) {
        return Spec.builder()
                .category(category)
                .title(title)
                .specData(specData)
                .build();
    }
}
