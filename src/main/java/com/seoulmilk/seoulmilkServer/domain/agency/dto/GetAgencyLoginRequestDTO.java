package com.seoulmilk.seoulmilkServer.domain.agency.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class GetAgencyLoginRequestDTO {

    @NotBlank
    @Schema(description = "아이디", example = "milksago1234")
    private String agencyId;

    @NotBlank
    @Schema(description = "비밀번호", example = "12345")
    private String password;

}
