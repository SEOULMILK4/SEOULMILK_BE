package com.seoulmilk.seoulmilkServer.domain.member.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdatePasswordRequestDTO {

    @NotBlank
    @Schema(description = "사원", example = "123456")
    private String employeeNum;

    @NotBlank
    @Schema(description = "새로운 비밀번호", example = "12345")
    private String password;
}
