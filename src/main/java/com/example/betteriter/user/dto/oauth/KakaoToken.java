package com.example.betteriter.user.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * - Kakao oauth server 부터 받아온 JWT
 * 1. token-type
 * 2. access-token
 * 3. refresh-token
 * 4. expires-in
 * 5. refresh-expires-in
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KakaoToken {
    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("scope")
    private String scope;
}
