package com.example.betteriter.global.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * - JwtAuthenticationEntryPoint : JwtAuthenticationFilter 에서 발생한 인증 예외(Authentication Exception(=401)) 처리
 * - commence() 메소드에서 구현
 **/
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        Object errorObject = request.getAttribute("UnauthorizedUserException");
        if (errorObject != null) {
            log.info("errorObject is Not Null");
            sendErrorUnauthorized(response);
        }
    }

    private void sendErrorUnauthorized(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.sendError(401, "잘못된 접근입니다.");
    }
}
