package com.seoulmilk.seoulmilkServer.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateOtpResponseDTO {

    @Schema(description = "사번", example = "123456")
    private final String employeeNum;

    @Schema(description = "메세지", example = "인증번호 발송 완료")
    private final String message;

    public static CreateOtpResponseDTO from(String employeeNum) {
        return CreateOtpResponseDTO.builder()
            .employeeNum(employeeNum)
            .message("인증번호 발송 완료")
            .build();
    }

}
