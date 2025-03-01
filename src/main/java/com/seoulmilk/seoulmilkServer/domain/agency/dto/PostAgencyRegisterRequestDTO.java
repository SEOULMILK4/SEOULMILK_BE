package com.seoulmilk.seoulmilkServer.domain.agency.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostAgencyRegisterRequestDTO {

    @NotBlank
    @Schema(description = "아이디", example = "milksago1234")
    private String agencyId;

    @NotBlank
    @Schema(description = "비밀번호", example = "12345")
    private String password;

    @NotBlank
    @Schema(description = "이메일", example = "cse0522@naver.com")
    private String email;

}
