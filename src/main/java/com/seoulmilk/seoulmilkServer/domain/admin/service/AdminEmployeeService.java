package com.seoulmilk.seoulmilkServer.domain.admin.service;

import com.seoulmilk.seoulmilkServer.domain.admin.domain.Admin;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.agency.GetEmployeeWithAgencyResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.employee.GetOneEmployeeResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.employee.PostEmployeeRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.employee.PostEmployeeResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.member.repository.MemberRepository;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import com.seoulmilk.seoulmilkServer.global.etc.MemberProperties;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminEmployeeService {

    private final MemberRepository memberRepository;
    private final AdminAuthService adminAuthService;
    private final MemberProperties memberProperties;
    private final PasswordEncoder encoder;

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

    // 사원 등록
    // 1. 신규 등록

    @Transactional
    public PostEmployeeResponseDTO postEmployeeRegister(PostEmployeeRequestDTO requestDTO) {

        Admin admin = adminAuthService.getCurrentAdmin();
        String employeeNum = requestDTO.getEmployeeNum();

        // 이미 존재하는 사원인지 체크하기
        if (memberRepository.existsByEmployeeNum(employeeNum)) {
            throw new BusinessException(ErrorCode.MEMBER_ALREADY_REGISTERED);
        }

        String encodePassword = encoder.encode(memberProperties.getDefaultPassword());
        Member employee = Member.of(employeeNum, requestDTO.getName(), requestDTO.getEmail(),
            encodePassword);
        memberRepository.save(employee);

        return PostEmployeeResponseDTO.of(employee);

    }


}


