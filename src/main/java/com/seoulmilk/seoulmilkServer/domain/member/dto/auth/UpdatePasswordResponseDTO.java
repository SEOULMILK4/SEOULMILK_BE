package com.seoulmilk.seoulmilkServer.domain.member.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdatePasswordResponseDTO {

    @Schema(description = "사원번호", example = "123456")
    private final String employeeNum;

    @Schema(description = "메세지", example = "비밀번호 변경이 완료되었습니다.")
    private final String message;

    public static UpdatePasswordResponseDTO from(String employeeNum) {
        return UpdatePasswordResponseDTO.builder()
            .employeeNum(employeeNum)
            .message("비밀번호 변경이 완료되었습니다.")
            .build();
    }
}