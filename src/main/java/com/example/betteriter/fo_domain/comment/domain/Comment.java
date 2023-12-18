package com.example.betteriter.fo_domain.comment.domain;


import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.global.common.entity.BaseEntity;
import com.example.betteriter.global.constant.Status;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Slf4j
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(name = "order_num", nullable = false)
    @ColumnDefault("1")
    private Integer orderNum; // 댓글 순서

    @Column(name = "group_id")
    private Integer groupId; // 댓글 그룹: 일반 댓글은 자신의 id, 대댓글은 root 댓글의 id

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status; // 댓글 상태: ACTIVE, INACTIVE, DELETED

    public boolean isDeleted() {
        return this.status == Status.DELETED;
    }
}
