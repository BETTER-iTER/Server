package com.example.betteriter.fo_domain.review.domain;

import com.example.betteriter.bo_domain.spec.domain.SpecData;
import com.example.betteriter.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ReviewSpecData extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    private SpecData specData;

    @Builder
    private ReviewSpecData(Review review, SpecData specData) {
        this.review = review;
        this.specData = specData;
    }
}
