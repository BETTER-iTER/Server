package com.example.betteriter.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /*
        ErrorCode는 다음과 같은 형식으로 작성합니다.

        1. Success 및 Common Error
            HTTP_STATUS: HTTP_STATUS 는 HttpStatus Enum 을 참고하여 작성합니다.
                ex) _OK, _BAD_REQUEST, _UNAUTHORIZED, _FORBIDDEN, _METHOD_NOT_ALLOWED, _INTERNAL_SERVER_ERROR
            CODE: [CATEGORY]_[HTTP_STATUS_CODE]
                ex) SUCCESS_200, COMMON_400, COMMON_401, COMMON_403, COMMON_405, COMMON_500

        2. Other Error
            HTTP_STATUS: 에러의 상황을 잘 들어내는 HttpStatus 를 작성합니다.
                ex) USER_NOT_FOUND, USER_ALREADY_EXISTS
            CODE: [CATEGORY]_[HTTP_STATUS_CODE]_[ERROR_CODE]의 형식으로 작성합니다.
                ex) BAD_REQUEST -> USER_400_001,
                    NOT_FOUND -> USER_404_001,
                    ALREADY_EXISTS -> USER_409_001
     */

    // Success
    _OK(HttpStatus.OK, "SUCCESS_200", "OK"),

    // Common Error & Global Error
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_401", "인증 과정에서 오류가 발생했습니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON_403", "금지된 요청입니다."),
    _METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON_405", "지원하지 않는 Http Method 입니다."),
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_500", "서버 에러가 발생했습니다."),

    _METHOD_ARGUMENT_ERROR(HttpStatus.BAD_REQUEST, "METHOD_ARGUMENT_ERROR", "올바르지 않은 클라이언트 요청값입니다."), // controller 에서 받은 요청 DTO 유효성 검증

    // Example (For Test)
    TEST_BAD_REQUEST(HttpStatus.BAD_REQUEST, "TEST_400", "잘못된 요청 입니다. (For Test)"),

    // User & Auth Error
    _USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND_400", "일치하는 회원 정보를 찾을 수 없습니다."),
    _PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, "AUTH_PASSWORD_NOT_MATCH_401", "비밀번호가 일치하지 않습니다."),
    _EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST, "AUTH_EMAIL_DUPLICATION_401", "이미 존재하는 이메일입니다."),
    _EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "AUTH_EMAIL_NOT_FOUND_401", "이메일이 존재하지 않습니다."),
    _AUTH_CODE_ALREADY_EXIT(HttpStatus.BAD_REQUEST, "AUTH_CODE_ALREADY_EXIST_401", "이미 인증 코드가 존재합니다."),
    _AUTH_CODE_NOT_EXIST(HttpStatus.BAD_REQUEST, "AUTH_CODE_NOT_EXIST_401", "인증 코드가 존재하지 않습니다."),
    _AUTH_CODE_NOT_MATCH(HttpStatus.BAD_REQUEST, "AUTH_CODE_NOT_MATCH_401", "인증 코드가 일치하지 않습니다."),
    _AUTH_SHOULD_BE_KAKAO(HttpStatus.BAD_REQUEST, "AUTH_SHOULD_BE_KAKAO_401", "해당 회원은 카카오 로그인 회원입니다."),

    // Review
    REVIEW_NOT_FOUND(HttpStatus.BAD_REQUEST, "REVIEW_400", "일치하는 리뷰 정보를 찾을 수 없습니다."),
    REVIEW_NOT_ACTIVATE(HttpStatus.BAD_REQUEST, "REVIEW_400", "삭제되었거나 비공개된 리뷰입니다."),

    // News
    _NEWS_NOT_FOUND(HttpStatus.BAD_REQUEST, "NEWS_400", "일치하는 뉴스 정보를 찾을 수 없습니다."),


    // Manufacturer
    _MANUFACTURER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MANUFACTURER_400", "일치하는 제조사 정보를 찾을 수 없습니다."),

    // Comment
    COMMENT_NOT_EXIST(HttpStatus.BAD_REQUEST, "COMMENT_400", "일치하는 댓글 정보를 찾을 수 없습니다."),
    COMMENT_NOT_HAVE(HttpStatus.BAD_REQUEST, "COMMENT_400", "해당 댓글을 작성한 유저가 아닙니다."),

    ;


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public static ErrorCode valueOf(HttpStatus httpStatus) {
        if (httpStatus == null) {
            throw new GeneralException("httpStatus must not be null");
        }

        return Arrays.stream(values())
                .filter(errorCode -> errorCode.getHttpStatus() == httpStatus)
                .findFirst()
                .orElseGet(() -> {
                    if (httpStatus.is4xxClientError()) {
                        return ErrorCode._BAD_REQUEST;
                    } else if (httpStatus.is5xxServerError()) {
                        return ErrorCode._INTERNAL_SERVER_ERROR;
                    } else {
                        return ErrorCode._OK;
                    }
                });
    }

    public String getMessage(Throwable e) {
        return this.getMessage(this.getMessage() + " - " + e.getMessage());
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }
}
