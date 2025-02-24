package com.seoulmilk.seoulmilkServer.domain.auth.service;

import com.seoulmilk.seoulmilkServer.domain.auth.dto.GetLoginRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.GetLoginResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.GetNewTokenResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.UpdatePasswordRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.PostOtpRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.PostVerifyOtpRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.member.repository.MemberRepository;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import com.seoulmilk.seoulmilkServer.global.security.SecurityUtils;
import com.seoulmilk.seoulmilkServer.global.jwt.provider.JwtProvider;
import com.seoulmilk.seoulmilkServer.global.mail.service.EmailService;
import com.seoulmilk.seoulmilkServer.global.mail.service.OTPService;
import jakarta.servlet.http.HttpSession;
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

    @Transactional
    public GetNewTokenResponseDTO getNewToken(String refreshToken) {

        Member member = memberRepository.findById(getCurrentMemberId())
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        jwtProvider.validateToken(refreshToken);
        String createdAccessToken = jwtProvider.generateAccessToken(member);
        String createdRefreshToken = jwtProvider.generateRefreshToken(member);

        return GetNewTokenResponseDTO.of(createdAccessToken, createdRefreshToken);
    }


    public void getMemberLogout() {
        // 현재 로그인된 사용자를 불러오고, 해당 사용자의 리프레시 토큰을 삭제한다.
    }

    public Long getCurrentMemberId() {
        return SecurityUtils.getCurrentMemberId();
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
        return GetLoginResponseDTO.of(member, createdAccessToken, createdRefreshToken);

    }


    @Transactional
    public void postOtp(PostOtpRequestDTO requestDTO) {

        String employeeNum = requestDTO.getEmployeeNum();
        String email = requestDTO.getEmail();

        Member member = memberRepository.findByEmployeeNumAndEmail(employeeNum, email)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_EMAIL_MISMATCH));

        emailService.sendOtp(email, otpService.generateOtp(employeeNum));

    }

    @Transactional
    public void postVerifyOtp(PostVerifyOtpRequestDTO requestDTO, HttpSession session) {

        String employeeNum = requestDTO.getEmployeeNum();
        String otpNum = requestDTO.getOtpNumber();

        if (!otpService.validateOtp(employeeNum, otpNum)) {
            throw new BusinessException(ErrorCode.OTP_INVALID);
        }
        session.setAttribute("verifiedEmployee", employeeNum);


    }

    @Transactional
    public void updatePassword(UpdatePasswordRequestDTO requestDTO, HttpSession session) {

        String employeeNum = (String) session.getAttribute("verifiedEmployee");

        if (employeeNum == null) {
            throw new BusinessException(ErrorCode.MEMBER_UNAUTHORIZED);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String newPassword = requestDTO.getPassword();

        Member member = memberRepository.findByEmployeeNum(employeeNum)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        member.updatePassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);

        session.removeAttribute("verifiedEmployee");

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

