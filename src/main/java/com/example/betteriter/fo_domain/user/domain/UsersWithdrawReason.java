package com.example.betteriter.fo_domain.user.domain;

import com.example.betteriter.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "USERS_WITHDRAW_REASON")
public class UsersWithdrawReason extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reason")
    private Integer reason;

    public UsersWithdrawReason(Integer reason) {
        this.reason = reason;
    }
}
