package com.seoulmilk.seoulmilkServer.domain.agency.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateAgencyOtpResponseDTO {

    @Schema(description = "대리점 아이디", example = "milksago4")
    private final String agencyId;

    @Schema(description = "메세지", example = "인증번호 발송 완료")
    private final String message;

    public static CreateAgencyOtpResponseDTO from(String agencyId) {
        return CreateAgencyOtpResponseDTO.builder()
            .agencyId(agencyId)
            .message("인증번호 발송 완료")
            .build();
    }

}
