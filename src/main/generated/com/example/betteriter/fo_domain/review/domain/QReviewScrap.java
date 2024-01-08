package com.example.betteriter.fo_domain.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewScrap is a Querydsl query type for ReviewScrap
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewScrap extends EntityPathBase<ReviewScrap> {

    private static final long serialVersionUID = -639618660L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewScrap reviewScrap = new QReviewScrap("reviewScrap");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QReview review;

    public final com.example.betteriter.fo_domain.user.domain.QUsers users;

    public QReviewScrap(String variable) {
        this(ReviewScrap.class, forVariable(variable), INITS);
    }

    public QReviewScrap(Path<? extends ReviewScrap> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewScrap(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewScrap(PathMetadata metadata, PathInits inits) {
        this(ReviewScrap.class, metadata, inits);
    }

    public QReviewScrap(Class<? extends ReviewScrap> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.review = inits.isInitialized("review") ? new QReview(forProperty("review"), inits.get("review")) : null;
        this.users = inits.isInitialized("users") ? new com.example.betteriter.fo_domain.user.domain.QUsers(forProperty("users"), inits.get("users")) : null;
    }

}

