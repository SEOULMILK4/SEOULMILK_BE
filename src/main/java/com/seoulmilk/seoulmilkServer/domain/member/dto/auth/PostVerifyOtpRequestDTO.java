package com.seoulmilk.seoulmilkServer.domain.member.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostVerifyOtpRequestDTO {

    @NotBlank
    @Schema(description = "사번", example = "123456")
    private String employeeNum;

    @NotBlank
    @Schema(description = "인증코드", example = "123456")
    private String otpNumber;
}
