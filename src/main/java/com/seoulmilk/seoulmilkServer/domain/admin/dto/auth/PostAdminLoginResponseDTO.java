package com.seoulmilk.seoulmilkServer.domain.admin.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostAdminLoginResponseDTO {

    @Schema(description = "액세스 토큰")
    private final String accessToken;

    @Schema(description = "리프레시 토큰")
    private final String refreshToken;

    @Schema(description = "메세지", example = "관리자 로그인 완료")
    private final String message;

    public static PostAdminLoginResponseDTO of(String accessToken, String refreshToken) {
        return PostAdminLoginResponseDTO.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .message("관리자 로그인 완료")
            .build();
    }

}
