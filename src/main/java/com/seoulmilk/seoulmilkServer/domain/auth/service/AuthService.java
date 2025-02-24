package com.seoulmilk.seoulmilkServer.domain.auth.service;

import com.seoulmilk.seoulmilkServer.domain.auth.dto.GetLoginRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.GetLoginResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.GetNewTokenResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.member.repository.MemberRepository;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import com.seoulmilk.seoulmilkServer.global.jwt.SecurityUtils;
import com.seoulmilk.seoulmilkServer.global.jwt.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final JwtProvider jwtProvider;

    public GetLoginResponseDTO getMemberLogin(GetLoginRequestDTO requestDTO) {

        // DB에서 해당 회원있는지 조회
        Member member = memberRepository.findByEmployeeNum(requestDTO.getEmployeeNum())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 비밀번호 검증
        if (!encoder.matches(requestDTO.getPassword(), member.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }

        // 토큰 생성해 반환
        return createToken(member);
    }

    private GetLoginResponseDTO createToken(Member member) {
        String createdAccessToken = jwtProvider.generateAccessToken(member);
        String createdRefreshToken = jwtProvider.generateRefreshToken(member);
        // redis에 저장
        return GetLoginResponseDTO.of(member, createdAccessToken, createdRefreshToken);
    }

    // 토큰 재발급
    public GetNewTokenResponseDTO getNewToken(String refreshToken) {
        Member member = memberRepository.findById(getCurrentMemberId())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        jwtProvider.validateToken(refreshToken);
        String createdAccessToken = jwtProvider.generateAccessToken(member);
        String createdRefreshToken = jwtProvider.generateRefreshToken(member);

        return GetNewTokenResponseDTO.of(createdAccessToken, createdRefreshToken);
    }


    // 로그아웃 처리
    public void getMemberLogout() {
        // 현재 로그인된 사용자를 불러오고, 해당 사용자의 리프레시 토큰을 삭제한다.
    }

    // 비번 해싱
    public void getHashedPassword() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String rawPassword = "12345"; // 저장할 비밀번호
        String hashedPassword = passwordEncoder.encode(rawPassword);

        System.out.println("Hashed Password: " + hashedPassword);
    }

    // 현재 로그인된 사용자 가져오기
    public Long getCurrentMemberId() {

        return SecurityUtils.getCurrentMemberId();
    }

    public void valifyToken(String token) {
        System.out.println(jwtProvider.getMemberEmployeeNumFromToken(token));
    }
}
