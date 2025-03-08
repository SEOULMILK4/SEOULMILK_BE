package com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response;

import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.IsSuccess;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetOcrNtsTaxListResponseDTO {
    private List<GetOcrNtsTaxResponseDTO> ocrNtsTaxList;
    private long totalCnt;
    private long successCnt;
    private long failedCnt;
    private long totalTimes;


    public static GetOcrNtsTaxListResponseDTO from(List<GetOcrNtsTaxResponseDTO> responseList, long totalTimes) {
        long totalCnt = responseList.size();
        long successCnt = responseList.stream()
                .filter(dto -> dto.getIsSuccess() == IsSuccess.SUCCESS)
                .count();
        long failedCnt = totalCnt - successCnt;

        return GetOcrNtsTaxListResponseDTO.builder()
                .ocrNtsTaxList(responseList)
                .totalCnt(totalCnt)
                .successCnt(successCnt)
                .failedCnt(failedCnt)
                .totalTimes(totalTimes)
                .build();
    }
}
