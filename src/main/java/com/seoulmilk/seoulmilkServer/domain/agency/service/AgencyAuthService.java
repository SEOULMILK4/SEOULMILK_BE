package com.seoulmilk.seoulmilkServer.domain.agency.service;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.GetAgencyLoginRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.GetAgencyLoginResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.PostAgencyRegisterRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.PostAgencyRegisterResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.repository.AgencyRepository;
import com.seoulmilk.seoulmilkServer.domain.auth.domain.RefreshTokenEntity;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.GetNewTokenResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.repository.RefreshTokenRepository;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import com.seoulmilk.seoulmilkServer.global.jwt.provider.JwtProvider;
import com.seoulmilk.seoulmilkServer.global.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AgencyAuthService {

    private final AgencyRepository agencyRepository;
    private final PasswordEncoder encoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;


    @Transactional
    public PostAgencyRegisterResponseDTO postAgencyRegister(
        PostAgencyRegisterRequestDTO requestDTO) {

        Agency agency = agencyRepository.findByEmail(requestDTO.getEmail())
            .orElseThrow(() -> new BusinessException(ErrorCode.AGENCY_EMAIL_INVALID));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String newPassword = requestDTO.getPassword();

        agency.updateAgencyInfo(requestDTO.getAgencyId(), passwordEncoder.encode(newPassword));

        return PostAgencyRegisterResponseDTO.from(agency);
    }

    @Transactional
    public GetAgencyLoginResponseDTO getAgencyLogin(GetAgencyLoginRequestDTO requestDTO) {

        Agency agency = agencyRepository.findByAgencyId(requestDTO.getAgencyId())
            .orElseThrow(() -> new BusinessException(ErrorCode.AGENCY_NOT_FOUND));

        if (!encoder.matches(requestDTO.getPassword(), agency.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }
        return createToken(agency);
    }


    @Transactional
    public void getAgencyLogout() {

        Agency agency = getCurrentAgency();

        refreshTokenRepository.deleteById(String.valueOf(agency.getId())+":"+"agency");
    }

    @Transactional
    public GetNewTokenResponseDTO getNewToken(String refreshToken) {

        Agency agency = getCurrentAgency();

        jwtProvider.validateToken(refreshToken);
        String createdAccessToken = jwtProvider.generateAccessToken(agency.getId(), "agency");
        String createdRefreshToken = jwtProvider.generateRefreshToken(agency.getId(), "agency");

        return GetNewTokenResponseDTO.of(createdAccessToken, createdRefreshToken);
    }

    @Transactional(readOnly = true)
    public Agency getCurrentAgency() {

        return agencyRepository.findById(SecurityUtils.getCurrentUserId())
            .orElseThrow(() -> new BusinessException(ErrorCode.AGENCY_NOT_FOUND));
    }


    private GetAgencyLoginResponseDTO createToken(Agency agency) {

        String createdAccessToken = jwtProvider.generateAccessToken(agency.getId(), "agency");
        String createdRefreshToken = jwtProvider.generateRefreshToken(agency.getId(), "agency");

        refreshTokenRepository.save(
            new RefreshTokenEntity(String.valueOf(agency.getId()),"agency",createdRefreshToken));

        return GetAgencyLoginResponseDTO.of(agency, createdAccessToken, createdRefreshToken);

    }
}
