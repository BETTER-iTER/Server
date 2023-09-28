package com.example.betteriter.example.dto;

public class TempConverter {

    public static TempResponse.TempTestDto toTempTestDto() {
        return TempResponse.TempTestDto.builder()
                .testString("test success")
                .build();
    }

    public static TempResponse.TempExceptionDto toTempExceptionDto(Integer flag) {
        return TempResponse.TempExceptionDto.builder()
                .flag(flag)
                .build();
    }
}
