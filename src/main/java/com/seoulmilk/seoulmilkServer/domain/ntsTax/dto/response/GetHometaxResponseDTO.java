package com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response;

import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.ARAP;
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
public class GetHometaxResponseDTO {

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

    @Schema(description = "매출", example = "AR")
    private ARAP ar;

    @Schema(description = "승인상태")
    private Status status;

    @Schema(description = "생성일")
    private LocalDate createdAt;

    @Schema(description = "생성시간")
    private LocalTime createdTime;

    public static GetHometaxResponseDTO from(NtsTax ntsTax) {
        return GetHometaxResponseDTO.builder()
                .ntsTaxId(ntsTax.getId())
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
                .status(ntsTax.getStatus())
                .createdAt(ntsTax.getCreatedAt().toLocalDate())
                .createdTime(ntsTax.getCreatedAt().toLocalTime())
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetHometaxListResponseDTO {
        List<GetHometaxResponseDTO> hometaxList;
        Integer listSize;
        Integer totalPage;
        Long successElements;
        Long failedElements;
        Long totalElements;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchHometaxListResponseDTO {
        List<GetHometaxResponseDTO> hometaxList;
        Integer listSize;
        Integer totalPage;
        Long successElements;
        Long failedElements;
        Long totalElements;
    }

    public static GetHometaxListResponseDTO from(Page<NtsTax> hometaxList) {
        List<GetHometaxResponseDTO> getHometaxlist = hometaxList.stream()
                .map(GetHometaxResponseDTO::from).collect(Collectors.toList());

        return GetHometaxListResponseDTO.builder()
                .hometaxList(getHometaxlist)
                .listSize(getHometaxlist.size())
                .totalElements(hometaxList.getTotalElements())
                .totalPage(hometaxList.getTotalPages())
                .totalElements(hometaxList.getTotalElements())
                .build();
    }

    public static GetHometaxListResponseDTO of(Page<NtsTax> hometaxList, Long successCnt, Long failedCnt) {
        List<GetHometaxResponseDTO> getHometaxList = hometaxList.stream()
                .map(GetHometaxResponseDTO::from)
                .collect(Collectors.toList());

        return GetHometaxListResponseDTO.builder()
                .hometaxList(getHometaxList)
                .listSize(getHometaxList.size())
                .totalPage(hometaxList.getTotalPages())
                .successElements(successCnt)
                .failedElements(failedCnt)
                .totalElements(successCnt + failedCnt)
                .build();
    }

    public static SearchHometaxListResponseDTO ofSearch(Page<NtsTax> hometaxList, Long successCnt, Long failedCnt, Long totalCnt) {
        List<GetHometaxResponseDTO> searchHometaxList = hometaxList.stream()
                .map(GetHometaxResponseDTO::from)
                .collect(Collectors.toList());

        return SearchHometaxListResponseDTO.builder()
                .hometaxList(searchHometaxList)
                .listSize(searchHometaxList.size())
                .totalPage(hometaxList.getTotalPages())
                .successElements(successCnt)
                .failedElements(failedCnt)
                .totalElements(totalCnt)
                .build();
    }
}
