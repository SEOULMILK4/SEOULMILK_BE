package com.seoulmilk.seoulmilkServer.domain.admin.dto.employee;

import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostEmployeeResponseDTO {

    @Schema(description = "사번", example = "20423424")
    private final String employeeNum;

    @Schema(description = "사원명", example = "김우유")
    private final String name;

    @Schema(description = "사원 이메일", example = "seoulmilk@gmail.com")
    private final String email;

    @Schema(description = "메세지", example = "사원 등록 완료")
    private final String message;

    public static PostEmployeeResponseDTO of(Member employee) {
        return PostEmployeeResponseDTO.builder()
            .employeeNum(employee.getEmployeeNum())
            .name(employee.getName())
            .email(employee.getEmail())
            .message("사원 등록 완료")
            .build();
    }

}
