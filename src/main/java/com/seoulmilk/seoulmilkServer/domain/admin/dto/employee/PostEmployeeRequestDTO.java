package com.seoulmilk.seoulmilkServer.domain.admin.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostEmployeeRequestDTO {

    @NotNull
    @Schema(description = "사번", example = "20423424")
    private String employeeNum;

    @NotBlank
    @Schema(description = "사원명", example = "김우유")
    private String name;

    @NotBlank
    @Schema(description = "사원이메일", example = "seoulmilk@gmail.com")
    private String email;

}
