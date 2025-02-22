package com.seoulmilk.seoulmilkServer.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {


    //NOT_VALID
    NOT_VALID_ERROR(404, "잘못된 파라미터입니다.");


    private final int status;
    private final String message;

}