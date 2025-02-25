package com.seoulmilk.seoulmilkServer.domain.auth.service;

import com.seoulmilk.seoulmilkServer.domain.auth.domain.RefreshTokenEntity;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.GetLoginRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.GetLoginResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.GetNewTokenResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.repository.RefreshTokenRepository;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.member.repository.MemberRepository;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import com.seoulmilk.seoulmilkServer.global.jwt.provider.JwtProvider;
import com.seoulmilk.seoulmilkServer.global.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;


    @Transactional
    public GetLoginResponseDTO getMemberLogin(GetLoginRequestDTO requestDTO) {

        Member member = memberRepository.findByEmployeeNum(requestDTO.getEmployeeNum())
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        if (!encoder.matches(requestDTO.getPassword(), member.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }

        return createToken(member);
    }


    @Transactional
    public void getMemberLogout() {
        Member member = getCurrentMember();
        refreshTokenRepository.deleteById(member.getEmployeeNum());
    }

    @Transactional
    public GetNewTokenResponseDTO getNewToken(String refreshToken) {

        Member member = getCurrentMember();

        jwtProvider.validateToken(refreshToken);
        String createdAccessToken = jwtProvider.generateAccessToken(member);
        String createdRefreshToken = jwtProvider.generateRefreshToken(member);

        return GetNewTokenResponseDTO.of(createdAccessToken, createdRefreshToken);
    }

    public Member getCurrentMember() {
        return memberRepository.findById(SecurityUtils.getCurrentMemberId())
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }


    private GetLoginResponseDTO createToken(Member member) {

        String createdAccessToken = jwtProvider.generateAccessToken(member);
        String createdRefreshToken = jwtProvider.generateRefreshToken(member);

        refreshTokenRepository.save(
            new RefreshTokenEntity(member.getEmployeeNum(), createdRefreshToken));

        return GetLoginResponseDTO.of(member, createdAccessToken, createdRefreshToken);

    }

//    public void verifyToken(String token) {
//        System.out.println(jwtProvider.getMemberEmployeeNumFromToken(token));
//    }

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

