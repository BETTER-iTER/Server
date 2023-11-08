package com.example.betteriter.fo_domain.user.domain;


import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

/**
 * - follower : 유저를 팔로우 하는 유저
 * - followee : 유저가 팔로우 하는 유저
 **/

@Slf4j
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "FOLLOW")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower")
    private User follower; // 팔로우 하는 유저 id는 ( 나 )

    @ManyToOne
    @JoinColumn(name = "followee")
    private User followee; // 팔로우 당하는 유저 id ( 상대방 )

}
