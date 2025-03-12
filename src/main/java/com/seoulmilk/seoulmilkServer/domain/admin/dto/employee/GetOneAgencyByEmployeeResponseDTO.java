package com.seoulmilk.seoulmilkServer.domain.admin.dto.employee;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetOneAgencyByEmployeeResponseDTO {

    @Schema(description = "DB상 id", example = "12")
    private final Long id;

    @Schema(description = "대리점명", example = "서울우유 종각점")
    private final String name;

    public static GetOneAgencyByEmployeeResponseDTO from(Agency agency) {
        return GetOneAgencyByEmployeeResponseDTO.builder()
            .id(agency.getId())
            .name(agency.getAgencyName())
            .build();
    }
}
