package com.seoulmilk.seoulmilkServer.domain.agency.dto.register;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostAgencyVerifyOTPRequestDTO {
    @NotBlank
    @Schema(description = "이메일", example = "milksajo@gmail.com")
    private String email;

    @NotBlank
    @Schema(description = "인증코드", example = "384855")
    private String otpNumber;

}
