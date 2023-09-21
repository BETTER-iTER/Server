package com.example.betteriter.global.config.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * - Security Context Holder 에 주입되는 Authentication 을 구현한 AbstractAuthenticationToken
 * - UserAuthentication 을 통해 Security Context Holder 로 관리 가능
 **/

@Getter
public class UserAuthentication extends AbstractAuthenticationToken {
    public UserAuthentication(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
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
