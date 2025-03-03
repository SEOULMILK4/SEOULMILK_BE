package com.seoulmilk.seoulmilkServer.domain.agency.dto.register;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostAgencyOTPRequestDTO {
    @NotBlank
    @Schema(description = "이메일", example = "cse0522@naver.com")
    private String email;

}
