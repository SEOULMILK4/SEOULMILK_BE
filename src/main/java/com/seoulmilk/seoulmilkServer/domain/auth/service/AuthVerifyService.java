package com.seoulmilk.seoulmilkServer.domain.auth.service;

import com.seoulmilk.seoulmilkServer.domain.auth.domain.AuthVerifiedMember;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.CreateOtpRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.CreateOtpResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.PostVerifyOtpRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.PostVerifyOtpResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.UpdatePasswordRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.UpdatePasswordResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.repository.AuthVerifyRepository;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.member.repository.MemberRepository;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import com.seoulmilk.seoulmilkServer.global.mail.service.EmailService;
import com.seoulmilk.seoulmilkServer.global.mail.service.OTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthVerifyService {

    private final AuthVerifyRepository authVerifyRepository;
    private final MemberRepository memberRepository;
    private final EmailService emailService;
    private final OTPService otpService;
    private final AuthVerifyService authVerifyService;


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
        authVerifyService.isUserVerified(employeeNum);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String newPassword = requestDTO.getPassword();

        Member member = memberRepository.findByEmployeeNum(employeeNum)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        member.updatePassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);

        authVerifyService.removeVerifiedUser(employeeNum);

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

}


