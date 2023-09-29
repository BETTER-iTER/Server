package com.example.betteriter.infra;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailAuthenticationDto {
    private String email;
    private int code;
}
