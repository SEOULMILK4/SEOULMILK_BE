package com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class GetOcrTestResponseDTO {
    private List<String> extractedTexts;
}
