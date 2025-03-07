package com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OcrTaxInvoiceResponseDTO {

    @Schema(description = "세금계신서 id", example = "1")
    private final Long id;

    @Schema(description = "전위확인 결과", example = "1")
    private final String resAuthenticity;

//    @Schema(description = "전위확인 내용", example = "조회의 전자세금계산서는 2022년 01월 10일 발급된 사실이 있습니다.")
//    private final String resAuthenticityDesc;

    public static OcrTaxInvoiceResponseDTO of(Long id,String resAuthenticity) {
         return OcrTaxInvoiceResponseDTO.builder()
             .id(id)
             .resAuthenticity(resAuthenticity)
             .build();
     }

}
