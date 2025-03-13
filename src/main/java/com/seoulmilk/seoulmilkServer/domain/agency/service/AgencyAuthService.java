package com.seoulmilk.seoulmilkServer.domain.agency.service;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.agency.domain.ApprovedState;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.etc.ChangeAgencyPasswordRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.login.CreateAgencyOtpRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.login.CreateAgencyOtpResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.login.GetAgencyLoginRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.login.GetAgencyLoginResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.login.UpdateAgencyPasswordRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.login.UpdateAgencyPasswordResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.register.PostAgencyOTPRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.register.PostAgencyRegisterRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.register.PostAgencyRegisterResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.register.PostAgencyVerifyOTPRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.repository.AgencyRepository;
import com.seoulmilk.seoulmilkServer.global.auth.domain.AuthVerifiedMember;
import com.seoulmilk.seoulmilkServer.global.auth.domain.RefreshTokenEntity;
import com.seoulmilk.seoulmilkServer.domain.member.dto.auth.GetNewTokenResponseDTO;
import com.seoulmilk.seoulmilkServer.global.auth.repository.AuthVerifyRepository;
import com.seoulmilk.seoulmilkServer.global.auth.repository.RefreshTokenRepository;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import com.seoulmilk.seoulmilkServer.global.jwt.provider.JwtProvider;
import com.seoulmilk.seoulmilkServer.global.mail.service.EmailService;
import com.seoulmilk.seoulmilkServer.global.mail.service.OTPService;
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
    private final OTPService otpService;
    private final EmailService emailService;
    private final AuthVerifyRepository authVerifyRepository;


    @Transactional
    public PostAgencyRegisterResponseDTO postAgencyRegister(
        PostAgencyRegisterRequestDTO requestDTO) {

        Agency agency = agencyRepository.findByEmail(requestDTO.getEmail())
            .orElseThrow(() -> new BusinessException(ErrorCode.AGENCY_EMAIL_INVALID));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String newPassword = requestDTO.getPassword();

        agency.updateAgencyInfo(requestDTO.getAgencyId(), passwordEncoder.encode(newPassword));

        AuthVerifiedMember verifiedAgency = authVerifyRepository.findById(agency.getEmail())
            .orElseThrow(() -> new BusinessException(ErrorCode.AGENCY_NOT_VERIFIED));

        if (!verifiedAgency.isVerified()) {
            throw new BusinessException(ErrorCode.AGENCY_NOT_VERIFIED);
        }

        removeVerifiedUser(agency.getAgencyId());
        return PostAgencyRegisterResponseDTO.from(agency);
    }


    private void removeVerifiedUser(String agencyId) {
        authVerifyRepository.deleteById(agencyId);
    }

    @Transactional
    public GetAgencyLoginResponseDTO getAgencyLogin(GetAgencyLoginRequestDTO requestDTO) {

        Agency agency = agencyRepository.findByAgencyId(requestDTO.getAgencyId())
            .orElseThrow(() -> new BusinessException(ErrorCode.AGENCY_NOT_FOUND));

        if (!encoder.matches(requestDTO.getPassword(), agency.getPassword())) {
            throw new BusinessException(ErrorCode.AGENCY_LOGIN_FAILED);

        }
        return createToken(agency);
    }


    @Transactional
    public void getAgencyLogout() {
        Agency agency = getCurrentAgency();
        refreshTokenRepository.deleteById(agency.getId().toString() + ":" + "agency");
    }

    // 비밀번호 찾기
    @Transactional
    public CreateAgencyOtpResponseDTO createOTP(CreateAgencyOtpRequestDTO requestDTO) {
        String agencyId = requestDTO.getAgencyId();
        String agencyEmail = requestDTO.getEmail();

        Agency agency = agencyRepository.findByAgencyIdAndEmail(agencyId, agencyEmail)
            .orElseThrow(() -> new BusinessException(ErrorCode.AGENCY_EMAIL_MISMATCH));

        String createdOtp = otpService.generateOtp(agencyId);

        emailService.sendOtp(agencyEmail, createdOtp);
        storeVerifiedUser(agencyId, createdOtp);

        return CreateAgencyOtpResponseDTO.from(agencyId);
    }

    @Transactional
    public UpdateAgencyPasswordResponseDTO updatePassword(
        UpdateAgencyPasswordRequestDTO requestDTO) {

        String agencyId = requestDTO.getAgencyId();
        isUserVerified(agencyId);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String newPassword = requestDTO.getPassword();

        Agency agency = agencyRepository.findByAgencyId(agencyId)
            .orElseThrow(() -> new BusinessException(ErrorCode.AGENCY_NOT_FOUND));

        agency.updatePassword(passwordEncoder.encode(newPassword));
        agencyRepository.save(agency);

        removeVerifiedUser(agencyId);

        return UpdateAgencyPasswordResponseDTO.from(agencyId);

    }


    // 회원가입
    public void postAgencyCreateOtp(PostAgencyOTPRequestDTO requestDTO) {

        String agencyEmail = requestDTO.getEmail();

        Agency agency = agencyRepository.findByEmail(agencyEmail)
            .orElseThrow(() -> new BusinessException(ErrorCode.AGENCY_EMAIL_INVALID));

        // 회원가입 이력이 있는지 체크
        if (agency.getAgencyId() != null) {
            throw new BusinessException(ErrorCode.AGENCY_ALREADY_REGISTERED);
        }
        // 승인된 상태인지 체크
        if (agency.getApprovedState() == ApprovedState.DISAPPROVED) {
            throw new BusinessException(ErrorCode.AGENCY_DISAPPROVED);
        }

        String createdOtp = otpService.generateOtp(agencyEmail);
        emailService.sendOtp(agencyEmail, createdOtp);

        storeVerifiedUser(agency.getEmail(), createdOtp);
    }

    private void storeVerifiedUser(String email, String otpCode) {
        authVerifyRepository.save(new AuthVerifiedMember(email, otpCode));
    }

    @Transactional
    public void postVerifyOtp(PostAgencyVerifyOTPRequestDTO requestDTO) {

        String email = requestDTO.getEmail();
        String otpNum = requestDTO.getOtpNumber();

        verifyUser(email, otpNum);
    }

    private void verifyUser(String email, String otpCode) {
        AuthVerifiedMember agency = authVerifyRepository.findById(email)
            .orElseThrow(() -> new BusinessException(ErrorCode.AGENCY_NOT_FOUND));

        if (!agency.getOtpCode().equals(otpCode)) {
            throw new BusinessException(ErrorCode.OTP_INVALID);
        }

        agency.updateVerified();
        authVerifyRepository.save(agency);
    }

    private void isUserVerified(String agencyId) {
        AuthVerifiedMember agency = authVerifyRepository.findById(agencyId)
            .orElseThrow(() -> new BusinessException(ErrorCode.VERIFIED_AGENCY_NOT_FOUND));

        if (!agency.isVerified()) {
            throw new BusinessException(ErrorCode.VERIFIED_MEMBER_NOT_FOUND);
        }
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

        if (!"agency".equals(SecurityUtils.getCurrentUserRole())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        return agencyRepository.findById(SecurityUtils.getCurrentUserId())
            .orElseThrow(() -> new BusinessException(ErrorCode.AGENCY_NOT_FOUND));
    }


    private GetAgencyLoginResponseDTO createToken(Agency agency) {

        String createdAccessToken = jwtProvider.generateAccessToken(agency.getId(), "agency");
        String createdRefreshToken = jwtProvider.generateRefreshToken(agency.getId(), "agency");

        refreshTokenRepository.save(
            new RefreshTokenEntity(String.valueOf(agency.getId()), "agency", createdRefreshToken));

        return GetAgencyLoginResponseDTO.of(agency, createdAccessToken, createdRefreshToken);

    }

    @Transactional
    public void changePassword(ChangeAgencyPasswordRequestDTO requestDTO) {

        Agency agency = getCurrentAgency();

        String newPassword = requestDTO.getPassword();

        agency.updatePassword(encoder.encode(newPassword));
        agencyRepository.save(agency);

    }
}
