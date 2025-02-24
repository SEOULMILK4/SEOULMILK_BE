package com.seoulmilk.seoulmilkServer.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostVerifyOtpRequestDTO {

    @NotBlank
    @Schema(description = "사번", example = "123456")
    private String employeeNum;

    @NotBlank
    @Schema(description = "OTP번호", example = "123456")
    private String otpNumber;
}
