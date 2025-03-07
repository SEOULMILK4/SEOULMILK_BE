package com.seoulmilk.seoulmilkServer.domain.admin.controller;

import com.seoulmilk.seoulmilkServer.domain.admin.dto.auth.PostAdminLoginRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.auth.PostAdminLoginResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.service.AdminAuthService;
import com.seoulmilk.seoulmilkServer.domain.member.dto.auth.GetNewTokenResponseDTO;
import com.seoulmilk.seoulmilkServer.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "[관리자 - 인증 API]")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    @Operation(summary = "관리자 로그인")
    @PostMapping("/login")
    public ApiResponse<PostAdminLoginResponseDTO> getMemberRegister(
        @RequestBody @Valid PostAdminLoginRequestDTO registerDTO) {
        return ApiResponse.success(adminAuthService.postAdminLogin(registerDTO));
    }

    @Operation(summary = "관리자 로그아웃")
    @PostMapping("/logout")
    public ResponseEntity getAdminLogout() {
        adminAuthService.getAdminLogout();
        return ResponseEntity.ok().body("관리자 로그아웃 완료");
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/refresh")
    public ApiResponse<GetNewTokenResponseDTO> getTokenRefreshed(
        @RequestHeader(value = "refreshToken", required = false) String refreshToken) {
        return ApiResponse.success(adminAuthService.getNewToken(refreshToken));
    }
}
