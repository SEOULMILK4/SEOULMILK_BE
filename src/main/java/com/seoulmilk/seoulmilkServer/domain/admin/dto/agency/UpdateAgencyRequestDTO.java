package com.seoulmilk.seoulmilkServer.domain.admin.dto.agency;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateAgencyRequestDTO {

    @NotNull
    @Schema(description = "DB상 대리점 id", example = "1")
    private Long id;

    @NotBlank
    @Schema(description = "변경할 대리점 이메일", example = "newemail@gmail.com")
    private String email;

}
