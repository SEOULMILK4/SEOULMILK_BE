package com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OcrTaxInvoiceRequestDTO {


    @Schema(description = "공급자 사업등록번호", example = "4048112110")
    private String supplierRegNumber;

    @Schema(description = "공급 받는 자 사업자등록번호", example = "4048300734")
    private String contractorRegNumber;

    @Schema(description = "승인번호", example = "202012041000000085774313")
    private String approvalNo;

    @Schema(description = "세금 계산서 작성일자", example = "20201204")
    private String reportingDate;

    @Schema(description = "총 공급가액", example = "4909090")
    private String supplyValue;

}
