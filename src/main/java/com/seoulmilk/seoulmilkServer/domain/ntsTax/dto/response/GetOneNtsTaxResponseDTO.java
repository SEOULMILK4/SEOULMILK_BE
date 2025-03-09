package com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response;


import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.ARAP;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetOneNtsTaxResponseDTO {

    @Schema(description = "세금계산서 id", example = "1")
    private final Long ntsTaxId;

    @Schema(description = "승인번호", example = "20240630-06300630-06300201")
    private final String issueId;

    @Schema(description = "전자세금계산서 작성일자", example = "2024.06.30")
    private final String issueAt;

    @Schema(description = "공급자 사업등록번호", example = "306-28-70320")
    private final String suId;

    @Schema(description = "공급받는자 사업자 등록번호", example = "308-85-09085")
    private final String ipId;

    @Schema(description = "공급가액", example = "23,930,493")
    private final String chargeTotal;

    @Schema(description = "총셰액 합계", example = "23,179,824")
    private final String taxTotal;

    @Schema(description = "합계금액", example = "26,323,542")
    private final String grandTotal;

    @Schema(description = "매출매입구분", example = "AR")
    private final ARAP ar;

    @Schema(description = "생성일", example = "2025.02.22")
    private final String createdDate;

    @Schema(description = "생성시간", example = "13:00")
    private final String createdTime;

    @Schema(description = "이미지", example = "https://nts-tax.s3.ap-northeast-2.amazonaws.com/ocr-uploads/c6f3ce15-a08d-4c40-8c63-e52f05a9c058.pdf")
    private final String imageUrl;


    public static GetOneNtsTaxResponseDTO from(NtsTax ntsTax) {
        return GetOneNtsTaxResponseDTO.builder()
            .ntsTaxId(ntsTax.getId())
            .issueId(ntsTax.getIssueId())
            .issueAt(ntsTax.getIssueDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
            .suId(ntsTax.getSuId())
            .ipId(ntsTax.getIpId())
            .chargeTotal(ntsTax.getChargeTotal())
            .taxTotal(ntsTax.getTaxTotal())
            .grandTotal(ntsTax.getGrandTotal())
            .ar(ARAP.AR)
            .createdDate(ntsTax.getCreatedAt().toLocalDate()
                .format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
            .createdTime(
                ntsTax.getCreatedAt().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")))
            .imageUrl(ntsTax.getImageUrl())
            .build();

    }

}
