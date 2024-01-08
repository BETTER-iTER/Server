package com.example.betteriter.fo_domain.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewSpecData is a Querydsl query type for ReviewSpecData
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewSpecData extends EntityPathBase<ReviewSpecData> {

    private static final long serialVersionUID = 171492442L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewSpecData reviewSpecData = new QReviewSpecData("reviewSpecData");

    public final com.example.betteriter.global.common.entity.QBaseEntity _super = new com.example.betteriter.global.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QReview review;

    public final com.example.betteriter.bo_domain.spec.domain.QSpecData specData;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QReviewSpecData(String variable) {
        this(ReviewSpecData.class, forVariable(variable), INITS);
    }

    public QReviewSpecData(Path<? extends ReviewSpecData> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewSpecData(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewSpecData(PathMetadata metadata, PathInits inits) {
        this(ReviewSpecData.class, metadata, inits);
    }

    public QReviewSpecData(Class<? extends ReviewSpecData> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.review = inits.isInitialized("review") ? new QReview(forProperty("review"), inits.get("review")) : null;
        this.specData = inits.isInitialized("specData") ? new com.example.betteriter.bo_domain.spec.domain.QSpecData(forProperty("specData"), inits.get("specData")) : null;
    }

}

