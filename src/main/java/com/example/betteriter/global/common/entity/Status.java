package com.example.betteriter.global.common.entity;

import lombok.Getter;

@Getter
public enum Status {
    ACTIVE("활성화"),
    INACTIVE("비활성화"),
    DELETED("삭제");

    private String description;

    Status(String description) {
        this.description = description;
    }
}
