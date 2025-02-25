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

    @Schema(description = "사원번호", example = "123456")
    private final String employeeNum;

    @Schema(description = "메세지", example = "인증이 완료되었습니다.")
    private final String message;

    public static PostVerifyOtpResponseDTO from(String employeeNum) {
        return PostVerifyOtpResponseDTO.builder()
            .employeeNum(employeeNum)
            .message("인증이 완료되었습니다.")
            .build();
    }

}
