package com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.ARAP;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.IsSuccess;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@AllArgsConstructor
public class GetOcrNtsTaxResponseDTO {

    private Long memberId;

    private Long agencyId;

    @Schema(description = "OCR 성공/실패 여부", example = "SUCCESS")
    private IsSuccess isSuccess;

    @Schema(description = "세금 계산서 ID", example = "1")
    private Long ntsTaxId;

    @Schema(description = "승인번호", example = "20240630-06300630-06300201")
    private String issueId;

    @Schema(description = "세금 계산서 작성일자", example = "2025.02.01")
    private String issueDate;

    @Schema(description = "공급자 사업등록번호", example = "305-04-02042")
    private String suId;

    @Schema(description = "공급자 사업체명", example = "서울우유 대전 대리점")
    private String suName;

    @Schema(description = "공급 받는 자 사업자등록번호", example = "305-04-02042")
    private String ipId;

    @Schema(description = "공급 받는 자 사업체명", example = "부산 동구 참외 마트 왕십리점")
    private String ipName;

    @Schema(description = "합계 금액", example = "305,000")
    private String grandTotal;

    @Schema(description = "매출", example = "AR")
    private ARAP AR;

    @Schema(description = "총 공급가액", example = "305,000")
    private String chargeTotal;

    @Schema(description = "총 세액", example = "305,000")
    private String taxTotal;

    @Schema(description = "파일 이름")
    private String fileName;

    @Schema(description = "파일 URL")
    private String imageUrl;

    @Schema(description = "승인상태")
    private Status status;

    @Schema(description = "생성일")
    private LocalDate createdAt;

    @Schema(description = "생성시간")
    private LocalTime createdTime;

    public static GetOcrNtsTaxResponseDTO from(Agency agency, NtsTax ntsTax) {
        return GetOcrNtsTaxResponseDTO.builder()
            .isSuccess(ntsTax.getIsSuccess())
            .AR(ntsTax.getARAP())
            .memberId(agency.getMember().getId())
            .agencyId(agency.getId())
            .status(ntsTax.getStatus())
            .ntsTaxId(ntsTax.getId())
            .issueId(removeSpace(ntsTax.getIssueId()))
            .issueDate(formatIssueDate(ntsTax.getIssueDate()))
            .suId(removeSpace(ntsTax.getSuId()))
            .suName(removeSpace(ntsTax.getSuName()))
            .ipId(removeSpace(ntsTax.getIpId()))
            .ipName(removeSpace(ntsTax.getIpName()))
            .grandTotal(removeSpace(ntsTax.getGrandTotal()))
            .chargeTotal(removeSpace(ntsTax.getChargeTotal()))
            .taxTotal(removeSpace(ntsTax.getTaxTotal()))
            .createdAt(ntsTax.getCreatedAt().toLocalDate())
            .createdTime(ntsTax.getCreatedAt().toLocalTime())
            .imageUrl(removeSpace(ntsTax.getImageUrl()))
            .fileName(removeSpace(ntsTax.getFileName()))
            .build();
    }

    private static String removeSpace(String value) {
        if (value == null || value.chars().allMatch(Character::isWhitespace)) {
            return "";
        }
        return value.trim().replaceAll("\\s+", "");
    }

    private static String formatIssueDate(LocalDate issueDate) {
        if (issueDate == null || issueDate.equals(LocalDate.of(1, 1, 1))) {
            return "";
        }
        return issueDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }
}
