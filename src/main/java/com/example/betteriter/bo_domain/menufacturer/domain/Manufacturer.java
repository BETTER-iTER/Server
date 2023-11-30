package com.example.betteriter.bo_domain.menufacturer.domain;


import com.example.betteriter.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "MANUFACTURER")
public class Manufacturer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "co_name", nullable = false, unique = true)
    private String coName;

    @Builder
    private Manufacturer(String coName) {
        this.coName = coName;
    }

    public static Manufacturer createManufacturer(String coName) {
        return Manufacturer.builder()
                .coName(coName)
                .build();
    }
}
