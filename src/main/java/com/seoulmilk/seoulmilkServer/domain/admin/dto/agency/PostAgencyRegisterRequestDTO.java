package com.seoulmilk.seoulmilkServer.domain.admin.dto.agency;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostAgencyRegisterRequestDTO {

    @NotNull
    @Schema(description = "대리점 이름", example = "서울우유 고양시 나옹점")
    private String agencyName;

    @NotBlank
    @Schema(description = "대리점 이메일", example = "seoulmilk@gmail.com")
    private String email;

}
