package com.example.betteriter.fo_domain.review.repository;

import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.global.constant.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findFirst7ByCategoryOrderByCreatedAtDesc(Category category);

    List<Review> findReviewByWriter(Users users);

    List<Review> findFirst7ByWriterInOrderByCreatedAtDesc(List<Users> writers);

    // sum(count (r.reviewLiked),count(r.reviewScraped))
    @EntityGraph(attributePaths = {"reviewScraped", "reviewLiked"})
    @Query("select r from REVIEW r " +
            "left join r.reviewScraped rs " +
            "left join r.reviewLiked rl group by r " +
            "ORDER BY COALESCE(SUM(rs.id), 0) + COALESCE(SUM(rl.id), 0) DESC")
    List<Review> findTop7ReviewHavingMostScrapedAndLiked(Pageable pageable);

    @Query("select r " +
            "from REVIEW r " +
            "left join r.reviewScraped rs " +
            "left join r.reviewLiked rl " +
            "where r.status != 'DELETED' " +
            "group by r " +
            "order by r.createdAt desc ")
    List<Review> findAllByWriterId(Long id);
}
