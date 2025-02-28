package com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response;

import lombok.*;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class GetOcrTestResponseDTO {
    private Map<String, String> extractedData;
}
