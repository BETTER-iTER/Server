package com.example.betteriter.fo_domain.user.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PasswordUtil {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /* 암호화 */
    public String encode(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    /* 비밀번호 동일성 체크 */
    public boolean isEqual(String inputPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(inputPassword, encodedPassword);
    }
}
