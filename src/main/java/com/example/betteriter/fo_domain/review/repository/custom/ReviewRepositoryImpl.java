package com.example.betteriter.fo_domain.review.repository.custom;

import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.global.constant.Category;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.betteriter.fo_domain.follow.domain.QFollow.follow;
import static com.example.betteriter.fo_domain.review.domain.QReview.review;
import static com.example.betteriter.fo_domain.user.domain.QUsers.users;
import static org.springframework.util.ObjectUtils.isEmpty;

public class ReviewRepositoryImpl implements CustomReviewRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public ReviewRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Slice<Review> findReviewsBySearch(String name, String sort, Pageable pageable, Category category, Boolean expert) {
        Slice<Review> reviews = null;

        switch (sort) {
            // 최신순
            case "latest":
                reviews = getReviewsByLatest(name, pageable, category, expert);
                break;
            // 좋아요 많은 순
            case "mostLiked":
                reviews = getReviewsByMostLiked(name, pageable, category, expert);
                break;
            // 스크랩 많은 순
            case "mostScraped":
                reviews = getReviewsByMostScraped(name, pageable, category, expert);
                break;
            // 리뷰 작성자 팔로워 많은 순
            case "mostFollowers":
                reviews = getReviewsByMostFollowers(name, pageable, category, expert);
        }
        return reviews;
    }

    private Slice<Review> getReviewsByMostFollowers(String name, Pageable pageable, Category category, Boolean expert) {

        NumberPath<Long> followerCount = (NumberPath<Long>) follow.id.count();

        List<Review> reviews = jpaQueryFactory.select(review)
                .from(review)
                .join(review.writer, users)
                .fetchJoin()
                .leftJoin(follow)
                .on(follow.followee.eq(review.writer))
                .where(review.productName.eq(name),
                        categoryEq(category),
                        expertEq(expert)
                )
                .groupBy(review)
                .orderBy(followerCount.desc(), review.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(reviews);
    }

    private Slice<Review> getReviewsByMostScraped(String name, Pageable pageable, Category category, Boolean expert) {

        List<Review> reviews = jpaQueryFactory.selectFrom(review)
                .where(review.productName.eq(name),
                        categoryEq(category),
                        expertEq(expert)
                )
                .orderBy(review.scrapedCount.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(reviews);
    }

    private Slice<Review> getReviewsByMostLiked(String name, Pageable pageable, Category category, Boolean expert) {

        List<Review> reviews = jpaQueryFactory.selectFrom(review)
                .where(review.productName.eq(name),
                        categoryEq(category),
                        expertEq(expert)
                )
                .orderBy(review.likedCount.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(reviews);
    }

    private Slice<Review> getReviewsByLatest(String name, Pageable pageable, Category category, Boolean expert) {

        List<Review> reviews = jpaQueryFactory.selectFrom(review)
                .where(review.productName.eq(name),
                        categoryEq(category),
                        expertEq(expert)
                )
                .orderBy(review.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(reviews);
    }

    private BooleanExpression categoryEq(Category category) {
        return isEmpty(category) ? null : review.category.eq(category);
    }

    private BooleanExpression expertEq(Boolean expert) {
        return isEmpty(expert) ? null : review.writer.isExpert.eq(expert);
    }
}
