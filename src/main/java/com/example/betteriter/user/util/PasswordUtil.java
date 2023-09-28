package com.example.betteriter.user.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PasswordUtil {
    public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$";
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public PasswordUtil(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /* 암호화 */
    public String encode(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    /* 비밀번호 동일성 체크 */
    public boolean isEqual(String password, String encodedPassword) {
        return bCryptPasswordEncoder.matches(password, encodedPassword);
    }
}
