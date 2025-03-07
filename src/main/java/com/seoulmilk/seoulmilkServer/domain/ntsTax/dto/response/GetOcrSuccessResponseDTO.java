package com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response;

import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GetOcrSuccessResponseDTO {
    private Long ntsTaxId;
    private boolean success;

    public static GetOcrSuccessResponseDTO from(NtsTax ntsTax, boolean success) {
        return GetOcrSuccessResponseDTO.builder()
                .ntsTaxId(ntsTax.getId())
                .success(success)
                .build();
    }
}
