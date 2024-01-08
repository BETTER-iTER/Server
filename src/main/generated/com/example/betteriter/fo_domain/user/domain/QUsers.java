package com.example.betteriter.fo_domain.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUsers is a Querydsl query type for Users
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUsers extends EntityPathBase<Users> {

    private static final long serialVersionUID = 1775879864L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUsers users = new QUsers("users");

    public final com.example.betteriter.global.common.entity.QBaseEntity _super = new com.example.betteriter.global.common.entity.QBaseEntity(this);

    public final ListPath<com.example.betteriter.global.constant.Category, EnumPath<com.example.betteriter.global.constant.Category>> categories = this.<com.example.betteriter.global.constant.Category, EnumPath<com.example.betteriter.global.constant.Category>>createList("categories", com.example.betteriter.global.constant.Category.class, EnumPath.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isExpert = createBoolean("isExpert");

    public final StringPath oauthId = createString("oauthId");

    public final StringPath password = createString("password");

    public final ListPath<com.example.betteriter.fo_domain.review.domain.ReviewLike, com.example.betteriter.fo_domain.review.domain.QReviewLike> reviewLikes = this.<com.example.betteriter.fo_domain.review.domain.ReviewLike, com.example.betteriter.fo_domain.review.domain.QReviewLike>createList("reviewLikes", com.example.betteriter.fo_domain.review.domain.ReviewLike.class, com.example.betteriter.fo_domain.review.domain.QReviewLike.class, PathInits.DIRECT2);

    public final ListPath<com.example.betteriter.fo_domain.review.domain.Review, com.example.betteriter.fo_domain.review.domain.QReview> reviews = this.<com.example.betteriter.fo_domain.review.domain.Review, com.example.betteriter.fo_domain.review.domain.QReview>createList("reviews", com.example.betteriter.fo_domain.review.domain.Review.class, com.example.betteriter.fo_domain.review.domain.QReview.class, PathInits.DIRECT2);

    public final ListPath<com.example.betteriter.fo_domain.review.domain.ReviewScrap, com.example.betteriter.fo_domain.review.domain.QReviewScrap> reviewScraps = this.<com.example.betteriter.fo_domain.review.domain.ReviewScrap, com.example.betteriter.fo_domain.review.domain.QReviewScrap>createList("reviewScraps", com.example.betteriter.fo_domain.review.domain.ReviewScrap.class, com.example.betteriter.fo_domain.review.domain.QReviewScrap.class, PathInits.DIRECT2);

    public final EnumPath<com.example.betteriter.global.constant.RoleType> roleType = createEnum("roleType", com.example.betteriter.global.constant.RoleType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final QUsersDetail usersDetail;

    public QUsers(String variable) {
        this(Users.class, forVariable(variable), INITS);
    }

    public QUsers(Path<? extends Users> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUsers(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUsers(PathMetadata metadata, PathInits inits) {
        this(Users.class, metadata, inits);
    }

    public QUsers(Class<? extends Users> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.usersDetail = inits.isInitialized("usersDetail") ? new QUsersDetail(forProperty("usersDetail")) : null;
    }

}

