package com.example.betteriter.fo_domain.user.domain;

import com.example.betteriter.fo_domain.user.dto.RoleType;
import com.example.betteriter.fo_domain.user.dto.oauth.KakaoJoinDto;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "USERS")
public class User implements UserDetails {
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

    // ======= 기타 회원 정보 ======== //
    @Column(name = "usr_nickname", unique = true)
    private String nickName;

    @Column(name = "usr_job")
    private int job;

    @Column(name = "usr_intersts")
    private String interests;

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

    public void completeKakaoJoin(KakaoJoinDto kakaoJoinDto) {
        nickName = kakaoJoinDto.getNickname();
        job = kakaoJoinDto.getJob();
        interests = kakaoJoinDto.getInterests();
    }
}
