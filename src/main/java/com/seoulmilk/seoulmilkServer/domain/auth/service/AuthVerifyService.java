package com.seoulmilk.seoulmilkServer.domain.auth.service;

import com.seoulmilk.seoulmilkServer.domain.auth.domain.AuthVerifiedMember;
import com.seoulmilk.seoulmilkServer.domain.auth.repository.AuthVerifyRepository;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthVerifyService {

    private final AuthVerifyRepository authVerifyRepository;

    @Transactional
    public void storeVerifiedUser(String employeeNum, String otpCode) {
        authVerifyRepository.save(new AuthVerifiedMember(employeeNum, otpCode));
    }

    @Transactional
    public void verifyUser(String employNum,String otpCode) {
        AuthVerifiedMember member = authVerifyRepository.findById(employNum)
            .orElseThrow(() -> new BusinessException(ErrorCode.VERIFIED_MEMBER_NOT_FOUND));

        // 저장된 OTP와 입력된 OTP 비교
        if (!member.getOtpCode().equals(otpCode)){
            throw new BusinessException(ErrorCode.OTP_INVALID);
        }

        // 인증번호 일치 → verified 필드를 true로 변경하여 인증 완료 처리
        member.updateVerified();
        authVerifyRepository.save(member);
    }

    @Transactional
    public void isUserVerified(String employeeNum) {
        AuthVerifiedMember user = authVerifyRepository.findById(employeeNum)
            .orElseThrow(() -> new BusinessException(ErrorCode.VERIFIED_MEMBER_NOT_FOUND));

        if (!user.isVerified()) {
            throw new BusinessException(ErrorCode.VERIFIED_MEMBER_NOT_FOUND);
        }
    }

    @Transactional
    public void removeVerifiedUser(String employNum) {
        authVerifyRepository.deleteById(employNum);
    }

}


