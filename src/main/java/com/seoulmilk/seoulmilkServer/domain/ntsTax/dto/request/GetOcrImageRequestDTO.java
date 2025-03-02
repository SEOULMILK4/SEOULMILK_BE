package com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class GetOcrImageRequestDTO {
    private String format;
    private String name;
    private String data;
    private List<Integer> templateIds;
}
