package com.seoulmilk.seoulmilkServer.domain.admin.dto.agency;

import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetEmployeeListResponseDTO {
    private List<GetEmployeeWithAgencyResponseDTO> agencyList;
    private Integer listSize;
    private Integer totalPage;
    private Long totalElements;

    public static GetEmployeeListResponseDTO from(Page<GetEmployeeWithAgencyResponseDTO> members) {
        List<GetEmployeeWithAgencyResponseDTO> getMemberList = members.getContent();

        return GetEmployeeListResponseDTO.builder()
                .agencyList(getMemberList)
                .listSize(getMemberList.size())
                .totalPage(members.getTotalPages())
                .totalElements(members.getTotalElements())
                .build();
    }
}
