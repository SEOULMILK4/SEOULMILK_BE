package com.seoulmilk.seoulmilkServer.domain.agency.dto.login;

import com.seoulmilk.seoulmilkServer.domain.member.dto.auth.UpdatePasswordResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateAgencyPasswordResponseDTO {

    @Schema(description = "대리점 아이디", example = "milksajo4")
    private final String agencyId;

    @Schema(description = "메세지", example = "비밀번호 변경이 완료되었습니다.")
    private final String message;

    public static UpdateAgencyPasswordResponseDTO from(String agencyId) {
        return UpdateAgencyPasswordResponseDTO.builder()
            .agencyId(agencyId)
            .message("비밀번호 변경이 완료되었습니다.")
            .build();
    }

}
