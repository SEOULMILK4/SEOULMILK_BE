package com.seoulmilk.seoulmilkServer.domain.admin.dto.agency;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;

@Getter
public class InviteAgenciesRequestDTO {

    @NotEmpty
    @Schema(description = "이메일을 발송할 대리점 id(DB상 id)", example = "[1,3,4,5,9]")
    private List<Long> idList;

}
