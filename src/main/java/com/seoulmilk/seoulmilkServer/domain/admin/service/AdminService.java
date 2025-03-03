package com.seoulmilk.seoulmilkServer.domain.admin.service;

import com.seoulmilk.seoulmilkServer.domain.admin.domain.Admin;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.GetAgencyResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.GetEmployeeWithAgencyResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.GetOneEmployeeResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.UpdateAgencyRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.UpdateAgencyResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.agency.repository.AgencyRepository;
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
public class AdminService {

    private final MemberRepository memberRepository;
    private final AgencyRepository agencyRepository;
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


    @Transactional(readOnly = true)
    public List<GetAgencyResponseDTO> getAgencyList() {

        Admin admin = adminAuthService.getCurrentAdmin();

        List<Agency> agency = agencyRepository.findAll();

        return agency.stream()
            .map(GetAgencyResponseDTO::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public GetAgencyResponseDTO getOneAgency(Long agencyId) {

        Admin admin = adminAuthService.getCurrentAdmin();

        Agency agency = agencyRepository.findById(agencyId)
            .orElseThrow(() -> new BusinessException(ErrorCode.AGENCY_NOT_FOUND));

        return GetAgencyResponseDTO.from(agency);
    }


    @Transactional
    public UpdateAgencyResponseDTO updateAgencyInfo(UpdateAgencyRequestDTO requestDTO) {

        Admin admin = adminAuthService.getCurrentAdmin();

        Agency agency = agencyRepository.findById(requestDTO.getId())
            .orElseThrow(() -> new BusinessException(ErrorCode.AGENCY_NOT_FOUND));

        agency.updateAgencyEmail(requestDTO.getEmail());

        return UpdateAgencyResponseDTO.of(agency, requestDTO.getEmail());
    }

}


