package com.seoulmilk.seoulmilkServer.domain.admin.dto.employee;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetOneEmployeeResponseDTO {

    @Schema(description = "DB상 id", example = "12")
    private final Long id;

    @Schema(description = "사원이름", example = "김우유")
    private final String name;

    @Schema(description = "사번", example = "20123234")
    private final String employeeNum;

    @Schema(description = "이메일", example = "milksago@gmail.com")
    private final String email;

    @Schema(description = "담당 대리점 목록")
    private final List<GetOneAgencyByEmployeeResponseDTO> agencies;


    public static GetOneEmployeeResponseDTO from(Member member,List<GetOneAgencyByEmployeeResponseDTO> agencies) {
        return GetOneEmployeeResponseDTO.builder()
            .id(member.getId())
            .name(member.getName())
            .employeeNum(member.getEmployeeNum())
            .email(member.getEmail())
            .agencies(agencies)
            .build();
    }



}
