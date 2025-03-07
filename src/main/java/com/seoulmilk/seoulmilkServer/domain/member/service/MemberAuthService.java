package com.seoulmilk.seoulmilkServer.domain.member.service;

import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.member.dto.auth.CreateOtpRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.member.dto.auth.CreateOtpResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.member.dto.auth.GetLoginRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.member.dto.auth.GetLoginResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.member.dto.auth.GetNewTokenResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.member.dto.auth.PostVerifyOtpRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.member.dto.auth.PostVerifyOtpResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.member.dto.auth.UpdatePasswordRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.member.dto.auth.UpdatePasswordResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.member.repository.MemberRepository;
import com.seoulmilk.seoulmilkServer.global.auth.domain.AuthVerifiedMember;
import com.seoulmilk.seoulmilkServer.global.auth.domain.RefreshTokenEntity;
import com.seoulmilk.seoulmilkServer.global.auth.repository.AuthVerifyRepository;
import com.seoulmilk.seoulmilkServer.global.auth.repository.RefreshTokenRepository;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import com.seoulmilk.seoulmilkServer.global.jwt.provider.JwtProvider;
import com.seoulmilk.seoulmilkServer.global.mail.service.EmailService;
import com.seoulmilk.seoulmilkServer.global.mail.service.OTPService;
import com.seoulmilk.seoulmilkServer.global.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberAuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthVerifyRepository authVerifyRepository;
    private final EmailService emailService;
    private final OTPService otpService;



    @Transactional
    public GetLoginResponseDTO getMemberLogin(GetLoginRequestDTO requestDTO) {

        Member member = memberRepository.findByEmployeeNum(requestDTO.getEmployeeNum())
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        if (!encoder.matches(requestDTO.getPassword(), member.getPassword())) {
            throw new BusinessException(ErrorCode.MEMBER_LOGIN_FAILED);
        }
        return createToken(member);
    }


    @Transactional
    public void getMemberLogout() {
        Member member = getCurrentMember();
        refreshTokenRepository.deleteById(String.valueOf(member.getId()) + ":" + "employee");
    }

    @Transactional
    public GetNewTokenResponseDTO getNewToken(String refreshToken) {

        Member member = getCurrentMember();

        jwtProvider.validateToken(refreshToken);
        String createdAccessToken = jwtProvider.generateAccessToken(member.getId(),"employee");
        String createdRefreshToken = jwtProvider.generateRefreshToken(member.getId(),"employee");

        return GetNewTokenResponseDTO.of(createdAccessToken, createdRefreshToken);
    }

    @Transactional(readOnly = true)
    public Member getCurrentMember() {

        if (!"employee".equals(SecurityUtils.getCurrentUserRole())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        return memberRepository.findById(SecurityUtils.getCurrentUserId())
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }


    private GetLoginResponseDTO createToken(Member member) {

        String createdAccessToken = jwtProvider.generateAccessToken(member.getId(),"employee");
        String createdRefreshToken = jwtProvider.generateRefreshToken(member.getId(),"employee");

        refreshTokenRepository.save(
            new RefreshTokenEntity(String.valueOf(member.getId()), "employee",createdRefreshToken));

        return GetLoginResponseDTO.of(member, createdAccessToken, createdRefreshToken);

    }


    @Transactional
    public CreateOtpResponseDTO createOtp(CreateOtpRequestDTO requestDTO) {

        String employeeNum = requestDTO.getEmployeeNum();
        String email = requestDTO.getEmail();

        Member member = memberRepository.findByEmployeeNumAndEmail(employeeNum, email)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_EMAIL_MISMATCH));

        String createdOtp = otpService.generateOtp(employeeNum);
        emailService.sendOtp(email, createdOtp);

        storeVerifiedUser(employeeNum, createdOtp);

        return CreateOtpResponseDTO.from(employeeNum);

    }

    @Transactional
    public PostVerifyOtpResponseDTO postVerifyOtp(PostVerifyOtpRequestDTO requestDTO) {

        String employeeNum = requestDTO.getEmployeeNum();
        String otpNum = requestDTO.getOtpNumber();

        verifyUser(employeeNum, otpNum);

        return PostVerifyOtpResponseDTO.from(employeeNum);
    }

    @Transactional
    public UpdatePasswordResponseDTO updatePassword(UpdatePasswordRequestDTO requestDTO) {

        String employeeNum = requestDTO.getEmployeeNum();
        isUserVerified(employeeNum);

        String newPassword = requestDTO.getPassword();

        Member member = memberRepository.findByEmployeeNum(employeeNum)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        member.updatePassword(encoder.encode(newPassword));
        memberRepository.save(member);

        removeVerifiedUser(employeeNum);

        return UpdatePasswordResponseDTO.from(employeeNum);

    }


    private void storeVerifiedUser(String employeeNum, String otpCode) {
        authVerifyRepository.save(new AuthVerifiedMember(employeeNum, otpCode));
    }

    private void verifyUser(String employNum, String otpCode) {

        AuthVerifiedMember member = authVerifyRepository.findById(employNum)
            .orElseThrow(() -> new BusinessException(ErrorCode.VERIFIED_MEMBER_NOT_FOUND));

        if (!member.getOtpCode().equals(otpCode)) {
            throw new BusinessException(ErrorCode.OTP_INVALID);
        }

        member.updateVerified();
        authVerifyRepository.save(member);
    }


    private void isUserVerified(String employeeNum) {

        AuthVerifiedMember user = authVerifyRepository.findById(employeeNum)
            .orElseThrow(() -> new BusinessException(ErrorCode.VERIFIED_MEMBER_NOT_FOUND));

        if (!user.isVerified()) {
            throw new BusinessException(ErrorCode.VERIFIED_MEMBER_NOT_FOUND);
        }
    }

    private void removeVerifiedUser(String employNum) {
        authVerifyRepository.deleteById(employNum);
    }



//    public void getHashedPassword() {     // 비번 해싱
//
//        String rawPassword = "12345"; // 저장할 비밀번호
//        String hashedPassword = encoder.encode(rawPassword);
//
//        System.out.println("Hashed Password: " + hashedPassword);
//    }

}

