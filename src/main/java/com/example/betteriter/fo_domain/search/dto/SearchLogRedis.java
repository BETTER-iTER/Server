package com.example.betteriter.fo_domain.search.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchLogRedis {
    private String name;
    private String createdAt;
}
