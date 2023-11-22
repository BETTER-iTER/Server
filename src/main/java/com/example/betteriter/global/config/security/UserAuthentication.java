package com.example.betteriter.global.config.security;

import com.example.betteriter.fo_domain.user.domain.Users;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

/**
 * - Security Context Holder 에 주입되는 Authentication 을 구현한 AbstractAuthenticationToken
 * - 인증 후 UserAuthentication 을 SecurityContext 에 저장 시, Security Context Holder 로 어디서든 접근 가능 !!
 **/

@Getter
public class UserAuthentication extends AbstractAuthenticationToken {

    private final String userEmail;

    public UserAuthentication(Users users) {
        super(authorities(users));
        this.userEmail = users.getEmail();
    }

    private static List<GrantedAuthority> authorities(Users users) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(users.getRoleType().name()));
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userEmail;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }
}
