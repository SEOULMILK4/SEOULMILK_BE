package com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class GetCsvRequestDTO {

    @Schema(description = "DB상 id", example = "[1,3,4,5,9]")
    private List<Long> ntsTaxId;

}
