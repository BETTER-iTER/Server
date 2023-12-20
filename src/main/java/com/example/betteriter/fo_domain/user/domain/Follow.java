package com.example.betteriter.fo_domain.user.domain;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

/**
 * - follower : 유저를 팔로우 하는 유저
 * - followee : 유저가 팔로우 하는 유저
 **/

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "FOLLOW")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower")
    private Users follower; // 팔로우 하는 유저 id는 ( 나 )

    @ManyToOne
    @JoinColumn(name = "followee")
    private Users followee; // 팔로우 당하는 유저 id ( 상대방 )

    @Builder
    private Follow(Users follower, Users followee) {
        this.follower = follower;
        this.followee = followee;
    }
}
