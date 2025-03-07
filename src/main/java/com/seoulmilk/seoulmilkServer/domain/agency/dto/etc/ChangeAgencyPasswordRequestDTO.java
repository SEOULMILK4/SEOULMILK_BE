package com.seoulmilk.seoulmilkServer.domain.agency.dto.etc;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ChangeAgencyPasswordRequestDTO {

    @NotBlank
    @Schema(description = "새로운 비밀번호", example = "abc1234!")
    private String password;

}
