package com.example.betteriter.fo_domain.comment.domain;


import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.global.common.entity.BaseEntity;
import com.example.betteriter.global.constant.Status;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "status = 'ACTIVE'")
@Entity(name = "COMMENT")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "review_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status; // 댓글 상태: ACTIVE, INACTIVE, DELETED

    @Builder
    private Comment(Review review, Users users, String comment, Status status) {
        this.review = review;
        this.users = users;
        this.comment = comment;
        this.status = status;
    }

    public boolean isDeleted() {
        return this.status == Status.DELETED;
    }
}
