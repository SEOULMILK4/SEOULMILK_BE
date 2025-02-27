package com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response;

import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.ARAP;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetOcrResponseDTO {

    @Schema(description = "사용자 ID", example = "1")
    private Long memberId;
    @Schema(description = "세금 계산서 ID", example = "1")
    private Long ntsTaxId;
    @Schema(description = "승인번호", example = "2024-0630-0201")
    private String issueId;
    @Schema(description = "세금 계산서 작성일자", example = "2025-02-01")
    private LocalDate issueDate;
    @Schema(description = "공급자 사업등록번호", example = "305-04-02042")
    private String suId;
    @Schema(description = "공급 받는 자 사업자등록번호", example = "305-04-02042")
    private String ipId;
    @Schema(description = "합계 금액", example = "305,000")
    private Long grandTotal;
    @Schema(description = "매출", example = "AR")
    private ARAP AR;
    @Schema(description = "총 공급가액", example = "305,000")
    private Long chargeTotal;
    @Schema(description = "총 세액", example = "305,000")
    private Long taxTotal;
    @Schema(description = "생성일")
    private LocalDateTime erdAt;
    @Schema(description = "생성시간")
    private LocalTime erzEt;

    public static GetOcrResponseDTO from(Member member, NtsTax ntsTax) {
        return GetOcrResponseDTO.builder()
                .memberId(member.getId())
                .ntsTaxId(ntsTax.getId())
                .issueId(ntsTax.getIssueId())
                .issueDate(ntsTax.getIssueDate())
                .suId(ntsTax.getSuId())
                .ipId(ntsTax.getIpId())
                .grandTotal(ntsTax.getGrandTotal())
                .AR(ntsTax.getARAP())
                .chargeTotal(ntsTax.getChargeTotal())
                .taxTotal(ntsTax.getTaxTotal())
                .erdAt(ntsTax.getCreatedAt())
                .erzEt(ntsTax.getCreatedTime())
                .build();
    }
}
