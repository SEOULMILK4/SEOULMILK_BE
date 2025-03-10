package com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class ModifyNtsTaxRequestDTO {

    @Schema(description = "공급자명", example = "서울우유 대전 대리점")
    private String suName;

    @Schema(description = "공급받는자명", example = "부산 동구 참외 마트 왕십리점")
    private String ipName;

    @Schema(description = "승인번호", example = "20240630-06300630-06300201")
    private String issueId;

    @Schema(description = "전자세금계산서 작성일자", example = "2025.06.30")
    private LocalDate issueDate;

    @Schema(description = "공급자 사업등록번호", example = "305-04-02042")
    private String suId;

    @Schema(description = "공급 받는자 사업등록번호", example = "305-04-02042")
    private String ipId;

    @Schema(description = "공급가액", example = "26,323,542")
    private String chargeTotal;

    @Schema(description = "총세액 합계", example = "26,323,542")
    private String taxTotal;

    @Schema(description = "합계금액", example = "26,323,542")
    private String grandTotal;


}
