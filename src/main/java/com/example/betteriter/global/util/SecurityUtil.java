package com.example.betteriter.global.util;

import com.example.betteriter.global.config.security.UserAuthentication;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@NoArgsConstructor
@Component
public class SecurityUtil {
    public void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    public String getCurrentUserEmail() {
        UserAuthentication authentication
                = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
        return authentication.getUserEmail();
    }

    public Collection<GrantedAuthority> getUserAuthorities() {
        UserAuthentication authentication
                = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities();
    }
}
