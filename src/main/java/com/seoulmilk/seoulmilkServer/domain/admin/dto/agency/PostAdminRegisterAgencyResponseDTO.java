package com.seoulmilk.seoulmilkServer.domain.admin.dto.agency;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostAdminRegisterAgencyResponseDTO {

    @Schema(description = "대리점 이름", example = "서울우유 고양시 나옹점")
    private final String agencyName;

    @Schema(description = "대리점 이메일", example = "seoulmilk@gmail.com")
    private final String email;

    @Schema(description = "메세지", example = "대리점 등록 완료")
    private final String message;

    public static PostAdminRegisterAgencyResponseDTO of(Agency agency) {
        return PostAdminRegisterAgencyResponseDTO.builder()
            .agencyName(agency.getAgencyName())
            .email(agency.getEmail())
            .message("대리점 등록 완료")
            .build();
    }
}
