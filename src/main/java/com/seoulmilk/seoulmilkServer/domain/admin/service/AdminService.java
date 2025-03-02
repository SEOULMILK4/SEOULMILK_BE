package com.seoulmilk.seoulmilkServer.domain.admin.service;

import com.seoulmilk.seoulmilkServer.domain.admin.domain.Admin;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.GetAgencyListResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.GetEmployeeListResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.GetOneEmployeeResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.PostAdminLoginRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.PostAdminLoginResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.repository.AdminRepository;
import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.agency.repository.AgencyRepository;
import com.seoulmilk.seoulmilkServer.domain.auth.domain.RefreshTokenEntity;
import com.seoulmilk.seoulmilkServer.domain.auth.repository.RefreshTokenRepository;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.member.repository.MemberRepository;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import com.seoulmilk.seoulmilkServer.global.jwt.provider.JwtProvider;
import com.seoulmilk.seoulmilkServer.global.security.SecurityUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final MemberRepository memberRepository;
    private final AgencyRepository agencyRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder encoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public PostAdminLoginResponseDTO postAdminLogin(PostAdminLoginRequestDTO requestDTO) {

        Admin admin = adminRepository.findById(1L)
            .orElseThrow(() -> new BusinessException(ErrorCode.MASTERKEY_INVALID));

        if (!encoder.matches(requestDTO.getMasterKey(), admin.getMasterKey())) {
            throw new BusinessException(ErrorCode.MASTERKEY_INVALID);
        }
        return createToken(admin);
    }


    @Transactional
    public void getAdminLogout() {

        Admin admin = getCurrentAdmin();
        refreshTokenRepository.deleteById(String.valueOf(admin.getId()) + ":" + "admin");
    }

    @Transactional(readOnly = true)
    public Admin getCurrentAdmin() {

        return adminRepository.findById(SecurityUtils.getCurrentUserId())
            .orElseThrow(() -> new BusinessException(ErrorCode.ADMIN_NOT_FOUND));
    }


    private PostAdminLoginResponseDTO createToken(Admin admin) {

        String createdAccessToken = jwtProvider.generateAccessToken(admin.getId(), "admin");
        String createdRefreshToken = jwtProvider.generateRefreshToken(admin.getId(), "admin");

        refreshTokenRepository.save(
            new RefreshTokenEntity(String.valueOf(admin.getId()), "admin", createdRefreshToken));

        return PostAdminLoginResponseDTO.of(createdAccessToken, createdRefreshToken);
    }


    // 그 외 기능
    @Transactional(readOnly = true)
    public List<GetEmployeeListResponseDTO> getEmployeeList() {

        List<Member> memberList = memberRepository.findAll();

        return memberList.stream()
            .map(GetEmployeeListResponseDTO::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public GetOneEmployeeResponseDTO getOneEmployee(Long employeeId) {

        Member member = memberRepository.findById(employeeId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        return GetOneEmployeeResponseDTO.from(member);
    }

    @Transactional(readOnly = true)
    public List<GetAgencyListResponseDTO> getAgencyList() {

        List<Agency> agency = agencyRepository.findAll();

        return agency.stream()
            .map(GetAgencyListResponseDTO::from)
            .toList();
    }

}


