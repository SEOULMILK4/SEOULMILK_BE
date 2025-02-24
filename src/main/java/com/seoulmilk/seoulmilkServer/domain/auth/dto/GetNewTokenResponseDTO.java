package com.seoulmilk.seoulmilkServer.domain.auth.dto;

import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetNewTokenResponseDTO {

    @Schema(description = "액세스 토큰")
    private final String accessToken;

    @Schema(description = "리프레시 토큰")
    private final String refreshToken;

    public static GetNewTokenResponseDTO of(String accessToken, String refreshToken) {
        return GetNewTokenResponseDTO.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

}
