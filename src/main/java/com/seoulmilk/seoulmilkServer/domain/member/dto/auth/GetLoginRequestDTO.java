package com.seoulmilk.seoulmilkServer.domain.member.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class GetLoginRequestDTO {

    @NotBlank
    @Schema(description = "사번", example = "123456")
    private String employeeNum;

    @NotBlank
    @Schema(description = "비밀번호", example = "12345")
    private String password;

}
