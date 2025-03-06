package com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteNtsTaxRequestDTO {
    private Long ntsTaxId;
}
