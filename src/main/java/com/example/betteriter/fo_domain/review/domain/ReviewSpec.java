package com.example.betteriter.fo_domain.review.domain;


import com.example.betteriter.bo_domain.spec.domain.Spec;
import com.example.betteriter.bo_domain.spec.domain.SpecData;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "REVIEW_SPEC")
public class ReviewSpec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "review_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    @JoinColumn(name = "spec_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Spec spec;

    @JoinColumn(name = "spec_data_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private SpecData specData;
}
