package com.seoulmilk.seoulmilkServer.global.common;

import com.seoulmilk.seoulmilkServer.global.error.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private final boolean success;
    private T data;       // 응답 데이터


    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data);
    }

    public static <T> ApiResponse<T> error(T data) {
        return new ApiResponse<>(false, data);
    }
}
