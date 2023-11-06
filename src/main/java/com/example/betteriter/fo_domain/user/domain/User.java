package com.example.betteriter.fo_domain.user.domain;

import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.domain.ReviewLike;
import com.example.betteriter.fo_domain.review.domain.ReviewScrap;
import com.example.betteriter.fo_domain.user.dto.RoleType;
import com.example.betteriter.global.common.entity.BaseEntity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "USERS")
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usr_oauth_id", unique = true)
    private String oauthId;

    @Column(name = "usr_email", nullable = false, unique = true)
    private String email;

    @Column(name = "usr_pwd", unique = true)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "usr_role")
    private RoleType role;

    @OneToMany(mappedBy = "follower")
    private List<Follow> following; // 해당 회원이 가지는 팔로워 수

    @OneToMany(mappedBy = "followee")
    private List<Follow> followee; // 해당 회원이 가지는 팔로잉 수

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ReviewScrap> reviewScraps; // 유저가 스크랩한 리뷰

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ReviewLike> reviewLikes; // 유저가 좋아요한 리뷰

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<Review> reviews; // 유저가 작성한 리뷰

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private UserDetail userDetail; // 유저 상세 정보


    /**
     * 권한 설정
     **/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(getRole().name()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
