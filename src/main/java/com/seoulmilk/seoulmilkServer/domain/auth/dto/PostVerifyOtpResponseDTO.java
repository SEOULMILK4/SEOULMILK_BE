package com.seoulmilk.seoulmilkServer.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostVerifyOtpResponseDTO {


    @Schema(description = "사원번호")
    private final String employeeNum;

    public static PostVerifyOtpResponseDTO fomr(String employeeNum) {
        return PostVerifyOtpResponseDTO.builder()
            .employeeNum(employeeNum)
            .build();
    }

}
