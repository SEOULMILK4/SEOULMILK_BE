package com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UpdateNtsTaxRequestDTO {

//    @Schema(description = "대리점 ID", example = "1")
//    private Long agencyId;

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

    @Schema(description = "총 공급가액", example = "305,000")
    private String chargeTotal;

    @Schema(description = "총 세액", example = "305,000")
    private String taxTotal;
}
