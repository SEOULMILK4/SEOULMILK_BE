package com.seoulmilk.seoulmilkServer.domain.admin.dto.agency;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetNtsTaxListResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetAgencyResponseDTO {

    @Schema(description = "DB상 id", example = "12")
    private final Long id;

    @Schema(description = "대리점명", example = "모모 대리점")
    private final String agencyName;

    @Schema(description = "아이디", example = "momo2025")
    private final String agencyId;

    @Schema(description = "이메일", example = "milksago@gmail.com")
    private final String email;

    @Schema(description = "가입상태", example = "승인")
    private final String status;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetAgencyListResponseDTO {

        private List<GetAgencyResponseDTO> agencyList;
        private Integer listSize;
        private Integer totalPage;
        private Long totalElements;
    }

    public static GetAgencyResponseDTO from(Agency agency) {
        return GetAgencyResponseDTO.builder()
            .id(agency.getId())
            .agencyName(agency.getAgencyName())
            .agencyId(agency.getAgencyId())
            .email(agency.getEmail())
            .status(agency.getAgencyId() == null ? "미승인" : "승인")
            .build();
    }

    public static GetAgencyListResponseDTO from(Page<Agency> agencies) {
        List<GetAgencyResponseDTO> getAgencyList = agencies.stream()
            .map(GetAgencyResponseDTO::from)
            .collect(Collectors.toList());

        return GetAgencyListResponseDTO.builder()
            .agencyList(getAgencyList)
            .listSize(getAgencyList.size())
            .totalPage(agencies.getTotalPages())
            .totalElements(agencies.getTotalElements())
            .build();
    }
}