package com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.ARAP;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCsvResponseDTO {

    @Schema(description = "승인번호", example = "20240630-06300630-06300201")
    @JsonProperty("승인번호")
    private String issueId;

    @Schema(description = "세금 계산서 작성일자", example = "2025-02-01")
    @JsonProperty("작성일자")
    private LocalDate issueDate;

    @Schema(description = "공급자 사업등록번호", example = "305-04-02042")
    @JsonProperty("공급자 사업자 등록 번호")
    private String suId;

    @Schema(description = "공급자 사업체명", example = "서울우유 대전 대리점")
    @JsonProperty("공급자 사업체명")
    private String suName;

    @Schema(description = "공급 받는 자 사업자등록번호", example = "305-04-02042")
    @JsonProperty("공급 받는 자 사업자 등록 번호")
    private String ipId;

    @Schema(description = "공급 받는 자 사업체명", example = "부산 동구 참외 마트 왕십리점")
    @JsonProperty("공급 받는 자 사업체명")
    private String ipName;

    @Schema(description = "합계 금액", example = "305,000")
    @JsonProperty("합계 금액")
    private String grandTotal;

    @Schema(description = "총 공급가액", example = "305,000")
    @JsonProperty("공급가액")
    private String chargeTotal;

    @Schema(description = "총 세액", example = "305,000")
    @JsonProperty("세액")
    private String taxTotal;

    @Schema(description = "매출", example = "AR")
    @JsonProperty("매출")
    private ARAP ar;

    @Schema(description = "승인상태")
    @JsonProperty("검증 결과")
    private String status;

    public static GetCsvResponseDTO from(NtsTax ntsTax) {
        return GetCsvResponseDTO.builder()
                .ar(ntsTax.getARAP())
                .issueId(ntsTax.getIssueId())
                .issueDate(ntsTax.getIssueDate())
                .suId(ntsTax.getSuId())
                .suName(ntsTax.getSuName())
                .ipId(ntsTax.getIpId())
                .ipName(ntsTax.getIpName())
                .grandTotal(ntsTax.getGrandTotal())
                .chargeTotal(ntsTax.getChargeTotal())
                .taxTotal(ntsTax.getTaxTotal())
                .status(ntsTax.getStatus().getName())
                .build();
    }
}
