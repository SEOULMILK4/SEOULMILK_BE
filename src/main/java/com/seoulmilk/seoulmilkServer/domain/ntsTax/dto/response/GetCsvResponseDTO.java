package com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.ARAP;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCsvResponseDTO {

    @Schema(description = "공급자명", example = "서울우유 대전 대리점")
    @JsonProperty("공급자명")
    private String suName;

    @Schema(description = "공급받는자명", example = "부산 동구 참외 마트 왕십리점")
    @JsonProperty("공급받는자명")
    private String ipName;

    @Schema(description = "승인번호", example = "20240630-06300630-06300201")
    @JsonProperty("승인번호")
    private String issueId;

    @Schema(description = "매출매입구분", example = "AR")
    @JsonProperty("매출매입구분")
    private ARAP ar;

    @Schema(description = "전자세금계산서 작성일자", example = "2025-02-01")
    @JsonProperty("전자세금계산서 작성일자")
    private LocalDate issueDate;

    @Schema(description = "공급자 사업자등록번호", example = "305-04-02042")
    @JsonProperty("공급자 사업자등록번호")
    private String suId;

    @Schema(description = "공급받는자 사업자등록번호", example = "305-04-02042")
    @JsonProperty("공급받는자 사업자등록번호")
    private String ipId;

    @Schema(description = "총 공급가액 합계", example = "305,000")
    @JsonProperty("총 공급가액 합계")
    private String chargeTotal;

    @Schema(description = "총 세액 합계", example = "305,000")
    @JsonProperty("총 세액 합계")
    private String taxTotal;

    @Schema(description = "총액(공급가액+세액)", example = "305,000")
    @JsonProperty("총액(공급가액+세액)")
    private String grandTotal;

    @Schema(description = "승인상태")
    @JsonProperty("검증 결과")
    private String status;

    public static GetCsvResponseDTO from(NtsTax ntsTax) {
        return GetCsvResponseDTO.builder()
            .suName(ntsTax.getSuName())
            .ipName(ntsTax.getIpName())
            .issueId(ntsTax.getIssueId())
            .ar(ntsTax.getARAP())
            .issueDate(ntsTax.getIssueDate())
            .suId(ntsTax.getSuId())
            .ipId(ntsTax.getIpId())
            .chargeTotal(ntsTax.getChargeTotal())
            .taxTotal(ntsTax.getTaxTotal())
            .grandTotal(ntsTax.getGrandTotal())
            .status(ntsTax.getStatus().getName())
            .build();
    }
}
