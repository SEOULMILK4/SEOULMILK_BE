package com.seoulmilk.seoulmilkServer.domain.agency.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateAgencyPasswordRequestDTO {

    @NotBlank
    @Schema(description = "대리점 아이디", example = "milksajo4")
    private String agencyId;

    @NotBlank
    @Schema(description = "새로운 비밀번호", example = "12345")
    private String password;

}
