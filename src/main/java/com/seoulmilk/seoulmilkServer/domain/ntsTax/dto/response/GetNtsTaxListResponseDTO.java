package com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response;

import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.ARAP;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.IsSuccess;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetNtsTaxListResponseDTO {

    @Schema(description = "OCR 성공/실패 여부", example = "SUCCESS")
    private IsSuccess isSuccess;

    @Schema(description = "세금 계산서 ID", example = "1")
    private Long ntsTaxId;

    @Schema(description = "승인번호", example = "20240630-06300630-06300201")
    private String issueId;

    @Schema(description = "세금 계산서 작성일자", example = "2025-02-01")
    private LocalDate issueDate;

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

    @Schema(description = "총 공급가액", example = "305,000")
    private String chargeTotal;

    @Schema(description = "총 세액", example = "305,000")
    private String taxTotal;

    @Schema(description = "이미지 URL", example = "https://nts-tax.s3.ap-northeast-2.amazonaws.com/ocr-uploads/c6f3ce15-a08d-4c40-8c63-e52f05a9c058.pdf")
    private String imageUrl;

    @Schema(description = "매출", example = "AR")
    private ARAP AR;

    @Schema(description = "승인상태")
    private Status status;

    @Schema(description = "생성일")
    private LocalDate createdAt;

    @Schema(description = "생성시간")
    private LocalTime createdTime;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchNtsTaxListResponseDTO {

        List<GetNtsTaxListResponseDTO> ntsTaxList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NtsTaxListResponseDTO {

        List<GetNtsTaxListResponseDTO> ntsTaxList;
        Integer listSize;
        Integer totalPage;
        Long successElements;
        Long failedElements;
    }

    public static GetNtsTaxListResponseDTO from(NtsTax ntsTax) {
        return GetNtsTaxListResponseDTO.builder()
            .isSuccess(ntsTax.getIsSuccess())
            .AR(ARAP.AR)
            .ntsTaxId((ntsTax.getId()))
            .issueId(removeSpace(ntsTax.getIssueId()))
            .issueDate(ntsTax.getIssueDate())
            .suId(removeSpace(ntsTax.getSuId()))
            .suName(removeSpace(ntsTax.getSuName()))
            .ipId(removeSpace(ntsTax.getIpId()))
            .ipName(removeSpace(ntsTax.getIpName()))
            .grandTotal(removeSpace(ntsTax.getGrandTotal()))
            .chargeTotal(removeSpace(ntsTax.getChargeTotal()))
            .taxTotal(removeSpace(ntsTax.getTaxTotal()))
            .imageUrl(removeSpace(ntsTax.getImageUrl()))
            .status(ntsTax.getStatus())
            .createdAt(ntsTax.getCreatedAt().toLocalDate())
            .createdTime(ntsTax.getCreatedAt().toLocalTime())
            .build();
    }

    private static String removeSpace(String value) {
        if (value == null || value.chars().allMatch(Character::isWhitespace)) {
            return "";
        }
        return value.trim().replaceAll("\\s+", "");
    }

    public static NtsTaxListResponseDTO of(Page<NtsTax> ntsTaxList, Long successCnt,
        Long failedCnt) {
        List<GetNtsTaxListResponseDTO> getNtsTaxList = ntsTaxList.stream()
            .map(GetNtsTaxListResponseDTO::from)
            .collect(Collectors.toList());

        return NtsTaxListResponseDTO.builder()
            .ntsTaxList(getNtsTaxList)
            .listSize(getNtsTaxList.size())
            .totalPage(ntsTaxList.getTotalPages())
            .successElements(successCnt)
            .failedElements(failedCnt)
            .build();
    }

    public static SearchNtsTaxListResponseDTO from(Page<NtsTax> ntsTaxList) {
        List<GetNtsTaxListResponseDTO> getNtsTaxList = ntsTaxList.stream()
            .map(GetNtsTaxListResponseDTO::from)
            .collect(Collectors.toList());

        return SearchNtsTaxListResponseDTO.builder()
            .ntsTaxList(getNtsTaxList)
            .listSize(getNtsTaxList.size())
            .totalPage(ntsTaxList.getTotalPages())
            .totalElements(ntsTaxList.getTotalElements())
            .build();
    }
}
