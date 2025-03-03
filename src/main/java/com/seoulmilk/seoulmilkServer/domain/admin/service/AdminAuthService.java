package com.seoulmilk.seoulmilkServer.domain.admin.service;

import com.seoulmilk.seoulmilkServer.domain.admin.domain.Admin;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.PostAdminLoginRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.PostAdminLoginResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.repository.AdminRepository;
import com.seoulmilk.seoulmilkServer.global.auth.domain.RefreshTokenEntity;
import com.seoulmilk.seoulmilkServer.global.auth.repository.RefreshTokenRepository;
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

public class AdminAuthService {

    private final AdminRepository adminRepository;
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
        refreshTokenRepository.deleteById(admin.getId().toString() + ":" + "admin");
    }

    @Transactional(readOnly = true)
    public Admin getCurrentAdmin() {
        if (!"admin".equals(SecurityUtils.getCurrentUserRole())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

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


}
