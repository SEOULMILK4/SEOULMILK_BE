package com.seoulmilk.seoulmilkServer.domain.agency.dto.register;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostAgencyRegisterResponseDTO {

    @Schema(description = "회원가입 완료된 아이디", example = "milksago1234")
    private final String agencyId;

    @Schema(description = "메세지", example = "회원가입이 완료되었습니다.")
    private final String message;

    public static PostAgencyRegisterResponseDTO from(Agency agency) {
        return PostAgencyRegisterResponseDTO.builder()
            .agencyId(agency.getAgencyId())
            .message("회원가입이 완료되었습니다.")
            .build();
    }

}
