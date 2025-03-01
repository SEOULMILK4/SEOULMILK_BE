package com.seoulmilk.seoulmilkServer.domain.admin.service;

import com.seoulmilk.seoulmilkServer.domain.admin.domain.Admin;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.GetAgencyListResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.GetEmployeeListResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.GetOneEmployeeResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.PostAdminLoginRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.repository.AdminRepository;
import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.agency.repository.AgencyRepository;
import com.seoulmilk.seoulmilkServer.domain.auth.repository.RefreshTokenRepository;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.member.repository.MemberRepository;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final MemberRepository memberRepository;
    private final AgencyRepository agencyRepository;
    private final PasswordEncoder encoder;
    private final RefreshTokenRepository refreshTokenRepository;

    public void postAdminLogin(PostAdminLoginRequestDTO requestDTO) {
        Admin admin = adminRepository.findById(1L)
            .orElseThrow(() -> new BusinessException(ErrorCode.MASTERKEY_INVALID));

        if (!encoder.matches(requestDTO.getMasterKey(), admin.getMasterKey())) {
            throw new BusinessException(ErrorCode.MASTERKEY_INVALID);
        }
    }

    public List<GetEmployeeListResponseDTO> getEmployeeList() {
        List<Member> memberList = memberRepository.findAll();

        return memberList.stream()
            .map(GetEmployeeListResponseDTO::from)
            .toList();
    }

    public GetOneEmployeeResponseDTO getOneEmployee(Long employeeId) {
        Member member = memberRepository.findById(employeeId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        return GetOneEmployeeResponseDTO.from(member);
    }

    public List<GetAgencyListResponseDTO> getAgencyList() {
        List<Agency> agency = agencyRepository.findAll();

        return agency.stream()
            .map(GetAgencyListResponseDTO::from)
            .toList();
    }

}


