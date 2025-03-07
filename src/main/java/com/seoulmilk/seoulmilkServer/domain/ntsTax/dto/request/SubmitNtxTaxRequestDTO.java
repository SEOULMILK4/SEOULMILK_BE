package com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;

@Getter
public class SubmitNtxTaxRequestDTO {

    @NotEmpty
    @Schema(description = "제출할 세금계산서 id 리스트", example = "[1,3,4,5,9]")
    private List<Long> idList;

}
