package com.seoulmilk.seoulmilkServer.domain.member.dto.auth;

import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetLoginResponseDTO {

    @Schema(description = "회원 id", example = "1")
    private final Long memberId;

    @Schema(description = "사번", example = "123456")
    private final String employeeNum;

    @Schema(description = "액세스 토큰")
    private final String accessToken;

    @Schema(description = "리프레시 토큰")
    private final String refreshToken;

    public static GetLoginResponseDTO of(Member member, String accessToken, String refreshToken) {
        return GetLoginResponseDTO.builder()
            .memberId(member.getId())
            .employeeNum(member.getEmployeeNum())
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

}
