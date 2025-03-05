package com.seoulmilk.seoulmilkServer.domain.agency.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateAgencyOtpRequestDTO {

    @NotBlank
    @Schema(description = "대리점 아이디", example = "seoulmilk4")
    private String agencyId;

    @NotBlank
    @Schema(description = "이메일", example = "cse0522@naver.com")
    private String email;

}
