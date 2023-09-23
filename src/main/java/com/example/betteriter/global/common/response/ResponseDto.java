package com.example.betteriter.global.common.response;

import com.example.betteriter.global.error.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class ResponseDto<T> {
    
    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T result;
    
    
    public static <T> ResponseDto<T> onSuccess(T result) {
        return new ResponseDto<>(true, "2000", "OK", result);
    }
    
    public static <T> ResponseDto<T> onFail(ErrorCode code, T result) {
        return new ResponseDto<>(false, code.getCode(), code.getMessage(), result);
    }
}
