package com.seoulmilk.seoulmilkServer.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostAdminLoginRequestDTO {

    @NotBlank
    @Schema(description = "관리자 마스터키", example = "kdsjfskd2241f")
    private String masterKey;

}
