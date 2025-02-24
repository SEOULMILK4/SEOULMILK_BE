package com.seoulmilk.seoulmilkServer.domain.auth.service;

import com.seoulmilk.seoulmilkServer.domain.auth.domain.RefreshTokenEntity;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.CreateOtpRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.GetLoginRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.GetLoginResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.GetNewTokenResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.PostVerifyOtpRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.UpdatePasswordRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.repository.RefreshTokenRepository;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.member.repository.MemberRepository;
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
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final JwtProvider jwtProvider;
    private final EmailService emailService;
    private final OTPService otpService;
    private final AuthVerifyService authVerifyService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public GetNewTokenResponseDTO getNewToken(String refreshToken) {

        Member member = getCurrentMember();

        jwtProvider.validateToken(refreshToken);
        String createdAccessToken = jwtProvider.generateAccessToken(member);
        String createdRefreshToken = jwtProvider.generateRefreshToken(member);

        return GetNewTokenResponseDTO.of(createdAccessToken, createdRefreshToken);
    }


    @Transactional
    public void getMemberLogout() {
        Member member = getCurrentMember();
        refreshTokenRepository.deleteById(member.getEmployeeNum());
    }

    public Member getCurrentMember() {
        return memberRepository.findById(SecurityUtils.getCurrentMemberId())
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public void verifyToken(String token) {
        System.out.println(jwtProvider.getMemberEmployeeNumFromToken(token));
    }

    @Transactional
    public GetLoginResponseDTO getMemberLogin(GetLoginRequestDTO requestDTO) {

        Member member = memberRepository.findByEmployeeNum(requestDTO.getEmployeeNum())
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        if (!encoder.matches(requestDTO.getPassword(), member.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }

        return createToken(member);
    }

    private GetLoginResponseDTO createToken(Member member) {

        String createdAccessToken = jwtProvider.generateAccessToken(member);
        String createdRefreshToken = jwtProvider.generateRefreshToken(member);

        // redis에 저장
        refreshTokenRepository.save(
            new RefreshTokenEntity(member.getEmployeeNum(), createdRefreshToken));

        return GetLoginResponseDTO.of(member, createdAccessToken, createdRefreshToken);

    }


    @Transactional
    public void createOtp(CreateOtpRequestDTO requestDTO) {

        String employeeNum = requestDTO.getEmployeeNum();
        String email = requestDTO.getEmail();

        Member member = memberRepository.findByEmployeeNumAndEmail(employeeNum, email)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_EMAIL_MISMATCH));

        String createdOtp = otpService.generateOtp(employeeNum);
        emailService.sendOtp(email, createdOtp);

        //redis에 사번이랑 인증번호 저장
        authVerifyService.storeVerifiedUser(employeeNum, createdOtp);

    }

    @Transactional
    public void postVerifyOtp(PostVerifyOtpRequestDTO requestDTO) {

        //        if (!otpService.validateOtp(employeeNum, otpNum)) {
        //            throw new BusinessException(ErrorCode.OTP_INVALID);
        //        }
        //        session.setAttribute("verifiedEmployee", employeeNum);

        String employeeNum = requestDTO.getEmployeeNum();
        String otpNum = requestDTO.getOtpNumber();

        authVerifyService.verifyUser(employeeNum, otpNum);
    }

    @Transactional
    public void updatePassword(UpdatePasswordRequestDTO requestDTO) {
        //String employeeNum = (String) session.getAttribute("verifiedEmployee");
        //        if (employeeNum == null) {
        //            throw new BusinessException(ErrorCode.MEMBER_UNAUTHORIZED);
        //        }
//        session.removeAttribute("verifiedEmployee");
        //redis
        String employeeNum = requestDTO.getEmployeeNum();
        authVerifyService.isUserVerified(employeeNum);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String newPassword = requestDTO.getPassword();

        Member member = memberRepository.findByEmployeeNum(employeeNum)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        member.updatePassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);

        authVerifyService.removeVerifiedUser(employeeNum);

    }

    // 비번 해싱
//    public void getHashedPassword() {
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//        String rawPassword = "12345"; // 저장할 비밀번호
//        String hashedPassword = passwordEncoder.encode(rawPassword);
//
//        System.out.println("Hashed Password: " + hashedPassword);
//    }
}

