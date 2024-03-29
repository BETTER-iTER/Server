package com.example.betteriter.global.common.code.status;

import com.example.betteriter.global.common.code.BaseErrorCode;
import com.example.betteriter.global.common.response.ResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

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

    // Common Error & Global Error
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTHENTICATION_401", "인증 과정에서 오류가 발생했습니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "AUTHORIZATION_403", "금지된 요청입니다."),
    _METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON_405", "지원하지 않는 Http Method 입니다."),
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_500", "서버 에러가 발생했습니다."),
    _METHOD_ARGUMENT_ERROR(HttpStatus.BAD_REQUEST, "METHOD_ARGUMENT_ERROR",
        "올바르지 않은 클라이언트 요청값입니다."), // controller 에서 받은 요청 DTO 유효성 검증

    // JWT Error
    _JWT_IS_NOT_EXIST(HttpStatus.UNAUTHORIZED, "JWT_IS_NOT_EXIST", "Authorization 헤더에 JWT 정보가 존재하지 않습니다."),
    _JWT_IS_NOT_VALID(HttpStatus.UNAUTHORIZED, "JWT_IS_NOT_VALID", "Access Token 이 유효하지 않습니다."),
    _JWT_REFRESH_TOKEN_IS_NOT_VALID(HttpStatus.UNAUTHORIZED, "JWT_REFRESH_TOKEN_IS_NOT_VALID",
        "Refresh Token 이 유효하지 않습니다."),
    _JWT_ACCESS_TOKEN_IS_VALID(HttpStatus.UNAUTHORIZED, "JWT_ACCESS_TOKEN_IS_VALID", "Access Token 이 유효합니다."),
    _JWT_REFRESH_TOKEN_IS_NOT_MATCH(HttpStatus.UNAUTHORIZED, "JWT_REFRESH_TOKEN_IS_NOT_MATCH",
        "Refresh Token 이 일치하지 않습니다."),

    // Example (For Test)
    TEST_BAD_REQUEST(HttpStatus.BAD_REQUEST, "TEST_400_001", "잘못된 요청 입니다. (For Test)"),

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
    _REVIEW_NOT_FOUND(HttpStatus.BAD_REQUEST, "REVIEW_NOT_FOUND_400", "일치하는 리뷰 정보를 찾을 수 없습니다."),
    _REVIEW_NOT_ACTIVATE(HttpStatus.BAD_REQUEST, "REVIEW_NOT_ACTIVATE_400", "삭제되었거나 비공개된 리뷰입니다."),
    _REVIEW_IMAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "REVIEW_IMAGE_NOT_FOUND_400", "일치하는 리뷰 이미지 정보를 찾을 수 없습니다."),
    _REVIEW_WRITER_IS_NOT_MATCH(HttpStatus.BAD_REQUEST, "REVIEW_WRITER_IS_NOT_MATCH_400", "리뷰의 작성자가 아닙니다."),
    _REVIEW_SPEC_DATA_NOT_FOUND(HttpStatus.BAD_REQUEST, "REVIEW_SPEC_DATA_NOT_FOUND_400", "리뷰 스펙 데이터를 찾을 수 없습니다."),
    _REVIEW_UPDATE_IMAGE_ARGUMENT_ERROR(HttpStatus.BAD_REQUEST, "REVIEW_UPDATE_IMAGE_ARGUMENT_ERROR_400",
        "리뷰 이미지 수정 요청값이 올바르지 않습니다."),

    // IMAGE
    _IMAGE_FILE_NAME_IS_NOT_EXIST(HttpStatus.BAD_REQUEST, "IMAGE_FILE_NAME_IS_NOT_EXIST", "이미지 파일 이름이 존재하지 않습니다."),
    _IMAGE_FILE_UPLOAD_FAILED(HttpStatus.BAD_REQUEST, "IMAGE_FILE_UPLOAD_FAILED", "이미지 파일 업로드에 실패했습니다."),
    _IMAGE_FILE_UPLOAD_REQUEST_IS_NOT_VALID(HttpStatus.BAD_REQUEST, "IMAGE_FILE_UPLOAD_IS_NOT_VALID",
        "이미지 파일 업로드 요청 형식이 올바르지 않습니다."),
    _IMAGE_FILE_IS_NOT_EXIST(HttpStatus.BAD_REQUEST, "IMAGE_FILE_IS_NOT_EXISTED", "이미지 파일이 존재하지 않습니다."),
    _IMAGE_FILE_DELETE_FAILED(HttpStatus.BAD_REQUEST, "IMAGE_FILE_DELETE_FAILED", "이미지 파일 삭제에 실패했습니다."),

    // REVIEW_LIKE
    _REVIEW_LIKE_NOT_FOUND(HttpStatus.BAD_REQUEST, "REVIEW_LIKE_NOT_FOUND_400", "일치하는 리뷰 좋아요 정보를 찾을 수 없습니다."),

    // REVIEW_SCRAP
    _REVIEW_SCRAP_NOT_FOUND(HttpStatus.BAD_REQUEST, "REVIEW_SCRAP_NOT_FOUND_400", "일치하는 리뷰 스크랩 정보를 찾을 수 없습니다."),

    // News
    _NEWS_NOT_FOUND(HttpStatus.BAD_REQUEST, "NEWS_400", "일치하는 뉴스 정보를 찾을 수 없습니다."),

    // Manufacturer
    _MANUFACTURER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MANUFACTURER_NOT_FOUND_400", "일치하는 제조사 정보를 찾을 수 없습니다."),

    // Comment
    _COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "COMMENT_NOT_FOUND", "일치하는 댓글 정보를 찾을 수 없습니다."),
    _COMMENT_NOT_HAVE(HttpStatus.BAD_REQUEST, "COMMENT_USER_NOT_MATCH", "해당 댓글을 작성한 유저가 아닙니다."),

    // Follow
    _FOLLOW_NOT_FOUND(HttpStatus.BAD_REQUEST, "FOLLOW_NOT_FOUND_400", "일치하는 팔로우 정보를 찾을 수 없습니다."),
    _FOLLOW_MYSELF(HttpStatus.BAD_REQUEST, "FOLLOW_MYSELF_400", "자기 자신을 팔로우할 수 없습니다."),
    _FOLLOW_ALREADY(HttpStatus.BAD_REQUEST, "FOLLOW_ALREADY_400", "이미 팔로우한 유저입니다."),
    _FOLLOW_NOT_MATCH(HttpStatus.BAD_REQUEST, "FOLLOW_NOT_MATCH_400", "팔로우한 유저가 아닙니다."),

    ;


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ResponseDto.ErrorReasonDto getReason() {
        return ResponseDto.ErrorReasonDto.builder()
            .isSuccess(false)
            .code(this.code)
            .message(this.message)
            .build();
    }

    @Override
    public ResponseDto.ErrorReasonDto getReasonHttpStatus() {
        return ResponseDto.ErrorReasonDto.builder()
            .httpStatus(this.httpStatus)
            .isSuccess(false)
            .code(this.code)
            .message(this.message)
            .build();
    }
}
