package com.seoulmilk.seoulmilkServer.domain.admin.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;

@Getter
public class PostAssignAgeciesRequestDTO {

    @NotEmpty
    @Schema(description = "배정할 대리점 id 리스트", example = "[1,3,5]")
    private List<Long> idList;




}
