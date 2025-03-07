package com.seoulmilk.seoulmilkServer.domain.admin.dto.agency;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateAgencyRequestDTO {

    @NotBlank
    @Schema(description = "변경할 대리점 이메일", example = "newemail@gmail.com")
    private String email;

}
