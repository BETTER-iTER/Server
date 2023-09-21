package com.example.betteriter.user.dto.info;

import java.util.Map;

/**
 * - https://kapi.kakao.com/user/me 로 가져온 사용자 정보 가공
 **/
public class KakaoOauthUserInfo {

    private final Map<String, Object> attributes;

    public KakaoOauthUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getProvider() {
        return "kakao";
    }

    public String getOauthId() {
        return String.valueOf(this.attributes.get("id"));
    }

    public String getKakaoEmail() {
        return String.valueOf(getKakaoAccount().get("email"));
    }

    public Map<String, Object> getKakaoAccount() {
        return (Map<String, Object>) attributes.get("kakao_account");
    }
}
