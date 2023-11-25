package com.example.betteriter.global.filter;

import com.example.betteriter.global.common.response.ApiResponse;
import com.example.betteriter.global.common.code.status.ErrorStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * - JwtAuthenticationEntryPoint : JwtAuthenticationFilter 에서 발생한 인증 예외(Authentication Exception(=401)) 처리
 * - commence() 메소드에서 구현
 **/
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static void makeResultResponse(
            HttpServletResponse response,
            Exception exception
    ) throws IOException {
        try (OutputStream os = response.getOutputStream()) {
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Seoul")));
            javaTimeModule.addSerializer(LocalDateTime.class, localDateTimeSerializer);
            ObjectMapper objectMapper = new ObjectMapper().registerModule(javaTimeModule);
            objectMapper.writeValue(os, ApiResponse.onFailure(ErrorStatus._UNAUTHORIZED.getCode(), exception.getClass().getSimpleName(), null));
            os.flush();
        }
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.error("Authentication Exception Occurs!");
        Exception exception = (Exception) request.getAttribute("exception");
        sendErrorUnauthorized(request, response, exception);
    }

    // 인증 안된 경우
    private void sendErrorUnauthorized(HttpServletRequest request,
                                       HttpServletResponse response,
                                       Exception exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json,charset=utf-8");
        makeResultResponse(response, exception);
    }
}
