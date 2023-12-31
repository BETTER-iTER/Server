package com.example.betteriter.global.filter;

import com.example.betteriter.global.common.code.status.ErrorStatus;
import com.example.betteriter.global.common.response.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/* 인증이 필요한 자원에 대한 요청 인증이 실패한 경우 AuthenticationException 발생한 경우 동작 */
@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    // 권한 없는 경우
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException
    ) throws IOException {
        log.error(accessDeniedException.getClass().getSimpleName() + " Occurs!");
        this.sendErrorAccessDenied(response);
    }

    private void sendErrorAccessDenied(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json,charset=utf-8");
        this.makeResultResponse(response);
    }

    private void makeResultResponse(HttpServletResponse response) throws IOException {
        try (OutputStream os = response.getOutputStream()) {
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Seoul")));
            javaTimeModule.addSerializer(LocalDateTime.class, localDateTimeSerializer);
            ObjectMapper objectMapper = new ObjectMapper().registerModule(javaTimeModule);
            objectMapper.writeValue(os, ResponseDto.onFailure(ErrorStatus._FORBIDDEN.getCode(), "Authorization Exception", null));
            os.flush();
        }
    }
}
