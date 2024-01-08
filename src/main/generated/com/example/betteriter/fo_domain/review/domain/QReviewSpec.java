package com.example.betteriter.fo_domain.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewSpec is a Querydsl query type for ReviewSpec
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewSpec extends EntityPathBase<ReviewSpec> {

    private static final long serialVersionUID = -436262768L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewSpec reviewSpec = new QReviewSpec("reviewSpec");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QReview review;

    public final com.example.betteriter.bo_domain.spec.domain.QSpec spec;

    public final com.example.betteriter.bo_domain.spec.domain.QSpecData specData;

    public QReviewSpec(String variable) {
        this(ReviewSpec.class, forVariable(variable), INITS);
    }

    public QReviewSpec(Path<? extends ReviewSpec> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewSpec(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewSpec(PathMetadata metadata, PathInits inits) {
        this(ReviewSpec.class, metadata, inits);
    }

    public QReviewSpec(Class<? extends ReviewSpec> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.review = inits.isInitialized("review") ? new QReview(forProperty("review"), inits.get("review")) : null;
        this.spec = inits.isInitialized("spec") ? new com.example.betteriter.bo_domain.spec.domain.QSpec(forProperty("spec")) : null;
        this.specData = inits.isInitialized("specData") ? new com.example.betteriter.bo_domain.spec.domain.QSpecData(forProperty("specData"), inits.get("specData")) : null;
    }

}

