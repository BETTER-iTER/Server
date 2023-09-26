package com.example.betteriter.example.dto;

import lombok.*;


public class TempResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TempTestDto {
        private String testString;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TempExceptionDto {
        Integer flag;
    }

}
