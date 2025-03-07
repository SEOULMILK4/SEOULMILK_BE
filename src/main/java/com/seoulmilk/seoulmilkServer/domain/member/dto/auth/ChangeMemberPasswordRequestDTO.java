package com.seoulmilk.seoulmilkServer.domain.member.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ChangeMemberPasswordRequestDTO {

    @NotBlank
    @Schema(description = "새로운 비밀번호", example = "abc1234!")
    private String password;


}
