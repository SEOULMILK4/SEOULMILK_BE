package com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response;

import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetNtsTaxListResponseDTO {
    private Long ntsTaxId;
    private String suName;
    private String ipName;
    private LocalDate issueDate;
    private String chargeTotal;
    private LocalDateTime createdAt;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NtsTaxListResponseDTO {
        List<GetNtsTaxListResponseDTO> ntsTaxList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    public static GetNtsTaxListResponseDTO from(NtsTax ntsTax) {
        return GetNtsTaxListResponseDTO.builder()
                .ntsTaxId(ntsTax.getId())
                .suName(ntsTax.getSuName())
                .ipName(ntsTax.getIpName())
                .issueDate(ntsTax.getIssueDate())
                .chargeTotal(ntsTax.getChargeTotal())
                .createdAt(ntsTax.getCreatedAt())
                .build();
    }

    public static NtsTaxListResponseDTO from(Page<NtsTax> ntsTaxList) {
        List<GetNtsTaxListResponseDTO> getNtsTaxList = ntsTaxList.stream()
                .map(GetNtsTaxListResponseDTO::from).collect(Collectors.toList());

        return NtsTaxListResponseDTO.builder()
                .ntsTaxList(getNtsTaxList)
                .listSize(ntsTaxList.getSize())
                .totalPage(ntsTaxList.getTotalPages())
                .totalElements(ntsTaxList.getTotalElements())
                .isFirst(ntsTaxList.isFirst())
                .isLast(ntsTaxList.isLast())
                .build();
    }
}
