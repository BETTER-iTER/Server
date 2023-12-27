package com.example.betteriter.global.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// 사용자 회원 가입 시 name 을 입력 받아 enum 생성 후 저장
// 조회 시에도 이름 + url

@Getter
@RequiredArgsConstructor
public enum Category {
    MOBILE_PHONE("휴대폰", "https://velog.velcdn.com/images/choidongkuen/post/947054e4-8856-4e5a-a974-4c6ab80e725f/image.png"),
    LAPTOP("노트북", "https://velog.velcdn.com/images/choidongkuen/post/f26480d4-cf1e-4ff2-b62a-f3bb4cc812ad/image.png"),
    PC("PC", "https://velog.velcdn.com/images/choidongkuen/post/2de4d05a-7b30-4f51-9ea6-917b50ba3959/image.png"),
    SMART_WATCH("스마트워치", "https://velog.velcdn.com/images/choidongkuen/post/74cbe130-8b7e-4137-bd2f-abaa5aa262fe/image.png"),
    TABLET("태블릿", "https://velog.velcdn.com/images/choidongkuen/post/9c7cbfe5-cd02-4177-9fdc-832694120f99/image.png"),
    MOUSE("마우스", "https://velog.velcdn.com/images/choidongkuen/post/c0c04f0e-79d5-4057-9491-1a1ddc04cc29/image.png"),
    KEYBOARD("키보드", "https://velog.velcdn.com/images/choidongkuen/post/9a3b3358-8006-4239-94b7-a2c89b0795a4/image.png"),
    HEADPHONES("헤드폰", "https://velog.velcdn.com/images/choidongkuen/post/5db00b8a-e16a-491f-800a-9cb86d341d5e/image.png"),
    SPEAKER("스피커", "https://velog.velcdn.com/images/choidongkuen/post/b0f162b5-eabc-4c41-a98d-750eaa226f77/image.png"),
    BATTERY_CHARGER("보조배터리", "https://velog.velcdn.com/images/choidongkuen/post/59eedd5c-4f27-4e43-b873-5b539d521311/image.png"),
    ACCESSORY("악세서리", "https://velog.velcdn.com/images/choidongkuen/post/b4202564-f549-4459-af56-01cae91f1f48/image.png"),
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

    @JsonCreator
    public static List<Category> of(List<String> categories) {
        List<Category> categoryList = new ArrayList<>();
        for (Category category : Category.values()) {
            if (categories.contains(category.getCategoryName())) {
                categoryList.add(category);
            }
        }
        return categoryList;
    }

    @JsonValue
    public String getCategoryName() {
        return categoryName;
    }
}
