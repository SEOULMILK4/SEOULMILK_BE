package com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class GetOcrRequestDTO {

    @Builder.Default
    private final String version = "V2";

    @Builder.Default
    private final String requestId = UUID.randomUUID().toString();

    @Builder.Default
    private final long timestamp = System.currentTimeMillis();

    private final List<GetOcrImageRequestDTO> images;
}
