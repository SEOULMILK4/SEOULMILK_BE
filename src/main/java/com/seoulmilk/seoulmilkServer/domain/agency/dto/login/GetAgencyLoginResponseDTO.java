package com.seoulmilk.seoulmilkServer.domain.agency.dto.login;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetAgencyLoginResponseDTO {

    @Schema(description = "로그인된 대리점 아이디", example = "milksago1234")
    private final String agencyId;

    @Schema(description = "대리점명", example = "서울우유 강동점")
    private final String name;

    @Schema(description = "대리점 이메일", example = "milksago@gmail.com")
    private final String email;

    @Schema(description = "액세스 토큰")
    private final String accessToken;

    @Schema(description = "리프레시 토큰")
    private final String refreshToken;

    public static GetAgencyLoginResponseDTO of(Agency agency, String accessToken,
        String refreshToken) {
        return GetAgencyLoginResponseDTO.builder()
            .agencyId(agency.getAgencyId())
            .name(agency.getAgencyName())
            .email(agency.getEmail())
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

}
