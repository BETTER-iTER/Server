package com.example.betteriter.global.filter;

import com.example.betteriter.global.common.code.status.ErrorStatus;
import com.example.betteriter.global.common.response.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.example.betteriter.global.common.code.status.ErrorStatus.*;

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
                         AuthenticationException authException
    ) throws IOException {
        log.error(authException.getClass().getSimpleName() + " Occurs!");
        this.sendErrorUnauthorized(request, response, authException);
    }

    private void sendErrorUnauthorized(HttpServletRequest request,
                                       HttpServletResponse response,
                                       Exception exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json,charset=utf-8");
        this.makeResultResponse(request, response, this.checkException(request));
    }

    private String checkException(HttpServletRequest request) {
        return (String) request.getAttribute("exception");
    }

    private void makeResultResponse(
            HttpServletRequest request,
            HttpServletResponse response,
            String exceptionName
    ) throws IOException {
        // HttpServletResponse.getOutputStream() : binary & text
        try (OutputStream os = response.getOutputStream()) {
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Seoul")));
            javaTimeModule.addSerializer(LocalDateTime.class, localDateTimeSerializer);
            ObjectMapper objectMapper = new ObjectMapper().registerModule(javaTimeModule);
            ErrorStatus errorStatus = this.getErrorStatus(exceptionName);
            objectMapper.writeValue(os, ResponseDto.onFailure(errorStatus.getCode(), exceptionName, null));
            os.flush();
        }
    }

    private ErrorStatus getErrorStatus(String exceptionName) {
        ErrorStatus errorStatus;
        if (exceptionName.equals("InsufficientAuthenticationException")) {
            errorStatus = _JWT_IS_NOT_EXIST;
        } else if (exceptionName.equals("JwtException")) {
            errorStatus = _JWT_IS_NOT_VALID;
        } else {
            errorStatus = USER_NOT_FOUND;
        }
        return errorStatus;
    }
}
