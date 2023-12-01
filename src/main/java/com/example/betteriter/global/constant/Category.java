package com.example.betteriter.global.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 사용자 회원 가입 시 name 을 입력 받아 enum 생성 후 저장
// 조회 시에도 이름 + url

@Getter
@RequiredArgsConstructor
public enum Category {
    MOBILE_PHONE("휴대폰", "휴대폰 이미지 URL"),
    LAPTOP("노트북", "노트북 이미지 URL"),
    PC("PC", "PC 이미지 URL"),
    SMART_WATCH("스마트워치", "스마트워치 이미지 URL"),
    TABLET("태블릿", "태블릿 이미지 URL"),
    MOUSE("마우스", "마우스 이미지 URL"),
    KEYBOARD("키보드", "키보드 이미지 URL"),
    HEADPHONES("헤드폰", "헤드폰 이미지 URL"),
    SPEAKER("스피커", "스피커 이미지 URL"),
    BATTERY_CHARGER("보조배터리", "보조배터리 URL"),
    ACCESSORY("악세서리", "악세서리 URL"),
    ETC("기타", "기타 URL");

    private final String categoryName;
    private final String imgUrl;

    /* 요청으로 들어온 문자열이 Enum 타입으로 역직렬화될때 메소드의 매개변수로 들어감 */
    @JsonCreator
    public static Category from(String name) {
        for (Category category : Category.values()) {
            if (category.getCategoryName().equals(name)) {
                return category;
            }
        }
        throw new IllegalArgumentException("일치하는 Category 정보가 존재하지 않습니다.");
    }

    @JsonValue
    public String getCategoryName() {
        return categoryName;
    }
}
