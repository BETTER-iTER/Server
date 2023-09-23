package com.example.betteriter.global.error;

import com.example.betteriter.global.common.response.ResponseDto;
import com.example.betteriter.global.error.exception.ErrorCode;
import com.example.betteriter.global.error.exception.GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<Object> validation(
            ConstraintViolationException e,
            WebRequest request
    ) {
        return handleExceptionInternal(e, ErrorCode._UNAUTHORIZED, request);
    }

    @ExceptionHandler
    public ResponseEntity<Object> general(
            GeneralException e,
            WebRequest request
    ) {
        return handleExceptionInternal(e, e.getErrorCode(), request);
    }

    @ExceptionHandler
    public ResponseEntity<Object> exception(
            Exception e,
            WebRequest request
    ) {
        return handleExceptionInternal(e, ErrorCode._INTERNAL_SERVER_ERROR, request);
    }

    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body,
            HttpHeaders headers, HttpStatus status,
            WebRequest request
    ) {
        logger.info("At exception handler");
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) request;
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();

        String contentType = httpServletRequest.getHeader("Content-Type");
        logger.info("Content-Type: {}", contentType);
        logger.error("Error Log: ", ex);
        return handleExceptionInternal(ex, ErrorCode.valueOf(status), headers, status, request);
    }

    private ResponseEntity<Object> handleExceptionInternal(
            Exception ex, ErrorCode errorCode,
            WebRequest request
    ) {
        return handleExceptionInternal(ex, errorCode, HttpHeaders.EMPTY, errorCode.getHttpStatus(), request);
    }

    private ResponseEntity<Object> handleExceptionInternal(
            Exception e, ErrorCode errorCode,
            HttpHeaders headers, HttpStatus status,
            WebRequest request
    ) {
        ResponseDto<Object> responseDto = ResponseDto.onFail(errorCode, null);
        return super.handleExceptionInternal(
                e,
                responseDto,
                headers,
                status,
                request
        );
    }

    private ResponseEntity<Object> handleExceptionInternalFalse(
            Exception e, ErrorCode errorCode,
            HttpHeaders headers, HttpStatus status,
            WebRequest request
    ) {
        ResponseDto<Object> responseDto = ResponseDto.onFail(errorCode, null);
        return super.handleExceptionInternal(
                e,
                responseDto,
                headers,
                status,
                request
        );
    }

}
