package com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response;

import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.ARAP;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class GetOcrResponseDTO {
    @Schema(description = "세금 계산서 ID")
    private Long ntsTaxId;
    @Schema(description = "승인번호")
    private String issueId;
    @Schema(description = "세금 계산서 작성일자")
    private LocalDate issueDate;
    @Schema(description = "공급자 사업등록번호")
    private String suId;
    @Schema(description = "공급 받는 자 사업자등록번호")
    private String ipId;
    @Schema(description = "합계 금액")
    private Long grandTotal;
    @Schema(description = "매출")
    private ARAP AR;
    @Schema(description = "총 공급가액")
    private Long chargeTotal;
    @Schema(description = "총 세액")
    private Long taxTotal;
    @Schema(description = "생성일")
    private LocalDate erdAt;
    @Schema(description = "생성시간")
    private LocalTime erzEt;
}
