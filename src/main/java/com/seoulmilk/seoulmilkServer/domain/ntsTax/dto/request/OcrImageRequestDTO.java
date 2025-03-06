package com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OcrImageRequestDTO {
    private String format;
    private String name;
    private String url;
    private List<Integer> templateIds;
}
