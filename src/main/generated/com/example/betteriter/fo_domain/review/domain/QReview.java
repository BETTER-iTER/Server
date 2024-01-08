package com.example.betteriter.fo_domain.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = -499884235L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final com.example.betteriter.global.common.entity.QBaseEntity _super = new com.example.betteriter.global.common.entity.QBaseEntity(this);

    public final StringPath badPoint = createString("badPoint");

    public final DatePath<java.time.LocalDate> boughtAt = createDate("boughtAt", java.time.LocalDate.class);

    public final EnumPath<com.example.betteriter.global.constant.Category> category = createEnum("category", com.example.betteriter.global.constant.Category.class);

    public final NumberPath<Long> clickCount = createNumber("clickCount", Long.class);

    public final StringPath comparedProductName = createString("comparedProductName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath goodPoint = createString("goodPoint");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> likedCount = createNumber("likedCount", Long.class);

    public final com.example.betteriter.bo_domain.menufacturer.domain.QManufacturer manufacturer;

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final StringPath productName = createString("productName");

    public final ListPath<com.example.betteriter.fo_domain.comment.domain.Comment, com.example.betteriter.fo_domain.comment.domain.QComment> reviewComment = this.<com.example.betteriter.fo_domain.comment.domain.Comment, com.example.betteriter.fo_domain.comment.domain.QComment>createList("reviewComment", com.example.betteriter.fo_domain.comment.domain.Comment.class, com.example.betteriter.fo_domain.comment.domain.QComment.class, PathInits.DIRECT2);

    public final ListPath<ReviewImage, QReviewImage> reviewImages = this.<ReviewImage, QReviewImage>createList("reviewImages", ReviewImage.class, QReviewImage.class, PathInits.DIRECT2);

    public final ListPath<ReviewLike, QReviewLike> reviewLiked = this.<ReviewLike, QReviewLike>createList("reviewLiked", ReviewLike.class, QReviewLike.class, PathInits.DIRECT2);

    public final ListPath<ReviewScrap, QReviewScrap> reviewScraped = this.<ReviewScrap, QReviewScrap>createList("reviewScraped", ReviewScrap.class, QReviewScrap.class, PathInits.DIRECT2);

    public final NumberPath<Long> scrapedCount = createNumber("scrapedCount", Long.class);

    public final StringPath shortReview = createString("shortReview");

    public final NumberPath<Long> shownCount = createNumber("shownCount", Long.class);

    public final ListPath<ReviewSpecData, QReviewSpecData> specData = this.<ReviewSpecData, QReviewSpecData>createList("specData", ReviewSpecData.class, QReviewSpecData.class, PathInits.DIRECT2);

    public final NumberPath<Double> starPoint = createNumber("starPoint", Double.class);

    public final EnumPath<com.example.betteriter.global.constant.Status> status = createEnum("status", com.example.betteriter.global.constant.Status.class);

    public final NumberPath<Integer> storeName = createNumber("storeName", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.example.betteriter.fo_domain.user.domain.QUsers writer;

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.manufacturer = inits.isInitialized("manufacturer") ? new com.example.betteriter.bo_domain.menufacturer.domain.QManufacturer(forProperty("manufacturer")) : null;
        this.writer = inits.isInitialized("writer") ? new com.example.betteriter.fo_domain.user.domain.QUsers(forProperty("writer"), inits.get("writer")) : null;
    }

}

