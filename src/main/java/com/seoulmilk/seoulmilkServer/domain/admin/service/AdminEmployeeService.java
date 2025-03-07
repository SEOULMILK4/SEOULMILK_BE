package com.seoulmilk.seoulmilkServer.domain.admin.service;

import com.seoulmilk.seoulmilkServer.domain.admin.domain.Admin;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.agency.GetEmployeeWithAgencyResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.employee.GetOneEmployeeResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.member.repository.MemberRepository;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminEmployeeService {

    private final MemberRepository memberRepository;
    private final AdminAuthService adminAuthService;


    @Transactional(readOnly = true)
    public List<GetEmployeeWithAgencyResponseDTO> getEmployeeList() {

        Admin admin = adminAuthService.getCurrentAdmin();

        List<GetEmployeeWithAgencyResponseDTO> employees = memberRepository.findAllMembersWithAgencyCount();

        return employees;
    }

    @Transactional(readOnly = true)
    public GetOneEmployeeResponseDTO getOneEmployee(Long employeeId) {

        Admin admin = adminAuthService.getCurrentAdmin();

        Member member = memberRepository.findById(employeeId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        return GetOneEmployeeResponseDTO.from(member);
    }


}


