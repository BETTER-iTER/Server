package com.example.betteriter.global.config.security;

import com.example.betteriter.user.domain.User;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

/**
 * - Security Context Holder 에 주입되는 Authentication 을 구현한 AbstractAuthenticationToken
 * - UserAuthentication 을 통해 Security Context Holder 로 관리 가능
 **/

@Getter
public class UserAuthentication extends AbstractAuthenticationToken {

    private final Long userId;

    public UserAuthentication(User user) {
        super(authorities(user));
        this.userId = user.getId();
    }

    private static List<GrantedAuthority> authorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
