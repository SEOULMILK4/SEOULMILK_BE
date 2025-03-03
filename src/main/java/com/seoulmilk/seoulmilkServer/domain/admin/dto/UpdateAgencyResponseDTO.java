package com.seoulmilk.seoulmilkServer.domain.admin.dto;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateAgencyResponseDTO {

    @Schema(description = "DB상 id", example = "12")
    private final Long id;

    @Schema(description = "대리점명", example = "모모 대리점")
    private final String agencyName;

    @Schema(description = "아이디", example = "momo2025")
    private final String agencyId;

    @Schema(description = "변경된 이메일", example = "milksago@gmail.com")
    private final String email;

    public static UpdateAgencyResponseDTO of(Agency agency,String newEmail) {
        return UpdateAgencyResponseDTO.builder()
            .id(agency.getId())
            .agencyName(agency.getAgencyName())
            .agencyId(agency.getAgencyId())
            .email(newEmail)
            .build();
    }
}
