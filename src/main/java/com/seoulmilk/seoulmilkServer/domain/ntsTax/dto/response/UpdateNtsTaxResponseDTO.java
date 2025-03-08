package com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response;

import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.ARAP;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.IsSuccess;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
@AllArgsConstructor
public class UpdateNtsTaxResponseDTO {

    @Schema(description = "OCR 성공/실패 여부", example = "SUCCESS")
    private IsSuccess isSuccess;

    @Schema(description = "승인번호", example = "20240630-06300630-06300201")
    private String issueId;

    @Schema(description = "세금 계산서 작성일자", example = "2025-02-01")
    private LocalDate issueDate;

    @Schema(description = "공급자 사업등록번호", example = "305-04-02042")
    private String suId;

    @Schema(description = "공급자 사업체명", example = "서울우유 대전 대리점")
    private String suName;

    @Schema(description = "공급 받는 자 사업자등록번호", example = "305-04-02042")
    private String ipId;

    @Schema(description = "공급 받는 자 사업체명", example = "부산 동구 참외 마트 왕십리점")
    private String ipName;

    @Schema(description = "매출", example = "AR")
    private ARAP AR;

    @Schema(description = "합계 금액", example = "305,000")
    private String grandTotal;

    @Schema(description = "총 공급가액", example = "305,000")
    private String chargeTotal;

    @Schema(description = "총 세액", example = "305,000")
    private String taxTotal;

    @Schema(description = "생성일")
    private LocalDate createdAt;

    @Schema(description = "생성시간")
    private LocalTime createdTime;

    @Schema(description = "수정일")
    private LocalDateTime updatedAt;

    public static UpdateNtsTaxResponseDTO from(NtsTax ntsTax) {
        return UpdateNtsTaxResponseDTO.builder()
                .isSuccess(IsSuccess.SUCCESS)
                .issueId(ntsTax.getIssueId())
                .issueDate(ntsTax.getIssueDate())
                .suId(ntsTax.getSuId())
                .suName(ntsTax.getSuName())
                .ipId(ntsTax.getIpId())
                .ipName(ntsTax.getIpName())
                .AR(ntsTax.getARAP())
                .grandTotal(ntsTax.getGrandTotal())
                .chargeTotal(ntsTax.getChargeTotal())
                .taxTotal(ntsTax.getTaxTotal())
                .createdAt(ntsTax.getCreatedAt().toLocalDate())
                .createdTime(ntsTax.getCreatedAt().toLocalTime())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
