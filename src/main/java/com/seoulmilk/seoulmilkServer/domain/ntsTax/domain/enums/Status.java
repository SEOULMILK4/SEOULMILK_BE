package com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    APPROVAL("일치"), REJECTION("불일치"), WAITING("보류");

    private final String name;
}
