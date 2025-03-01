package com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response;

import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.ARAP;
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
public class GetOcrResponseDTO {

    private boolean success;
    private String message;

    @Schema(description = "사용자 ID", example = "1")
    private Long memberId;
    @Schema(description = "세금 계산서 ID", example = "1")
    private Long ntsTaxId;
    @Schema(description = "승인번호", example = "20240630-06300630-06300201")
    private String issueId;
    @Schema(description = "세금 계산서 작성일자", example = "2025-02-01")
    private LocalDate issueDate;
    @Schema(description = "공급자 사업등록번호", example = "305-04-02042")
    private String suId;
    @Schema(description = "공급 받는 자 사업자등록번호", example = "305-04-02042")
    private String ipId;
    @Schema(description = "합계 금액", example = "305,000")
    private String grandTotal;
    @Schema(description = "매출", example = "AR")
    private ARAP AR;
    @Schema(description = "총 공급가액", example = "305,000")
    private String chargeTotal;
    @Schema(description = "총 세액", example = "305,000")
    private String taxTotal;
    @Schema(description = "세금계산서 파일 URL")
    private String imageUrl;
    @Schema(description = "생성일")
    private LocalDateTime erdAt;
    @Schema(description = "생성시간")
    private LocalTime erzEt;

    public static GetOcrResponseDTO from(NtsTax ntsTax, boolean success, String message) {
        return GetOcrResponseDTO.builder()
                .success(success)
                .message(message)
                .ntsTaxId(ntsTax != null ? ntsTax.getId() : null)
                .issueId(ntsTax != null ? ntsTax.getIssueId() : null)
                .issueDate(ntsTax != null ? ntsTax.getIssueDate() : null)
                .suId(ntsTax != null ? ntsTax.getSuId() : null)
                .ipId(ntsTax != null ? ntsTax.getIpId() : null)
                .AR(ntsTax != null ? ntsTax.getARAP() : null)
                .grandTotal(ntsTax != null ? ntsTax.getGrandTotal() : null)
                .chargeTotal(ntsTax != null ? ntsTax.getChargeTotal() : null)
                .taxTotal(ntsTax != null ? ntsTax.getTaxTotal() : null)
                .imageUrl(ntsTax != null ? ntsTax.getImageUrl() : null)
                .erdAt(ntsTax != null ? ntsTax.getCreatedAt() : null)
                .erzEt(ntsTax != null ? ntsTax.getCreatedTime() : null)
                .build();
    }
}
