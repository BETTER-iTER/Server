package com.example.betteriter.bo_domain.spec.domain;


import com.example.betteriter.global.common.entity.BaseEntity;
import com.example.betteriter.global.constant.Category;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "SPEC")
public class Spec extends BaseEntity {
    @OneToMany(mappedBy = "spec", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<SpecData> specData = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Category category; // 어떤 카테고리에 대한 스펙인지
    @Column(name = "spec_title")
    private String title;

    @Builder
    private Spec(Category category, String title) {
        this.category = category;
        this.title = title;
    }

    public static Spec createSpec(Category category, String title) {
        return Spec.builder()
                .category(category)
                .title(title)
                .build();
    }
}
