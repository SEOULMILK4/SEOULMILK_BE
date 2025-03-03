package com.seoulmilk.seoulmilkServer.domain.member.controller;

import com.seoulmilk.seoulmilkServer.domain.member.dto.auth.CreateOtpRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.member.dto.auth.CreateOtpResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.member.dto.auth.GetLoginRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.member.dto.auth.GetLoginResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.member.dto.auth.GetNewTokenResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.member.dto.auth.PostVerifyOtpRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.member.dto.auth.PostVerifyOtpResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.member.dto.auth.UpdatePasswordRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.member.dto.auth.UpdatePasswordResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.member.service.MemberAuthService;
import com.seoulmilk.seoulmilkServer.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employee")
@Tag(name = "[사원 - 인증 API]")
public class MemberAuthController {

    private final MemberAuthService memberAuthService;

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ApiResponse<GetLoginResponseDTO> getMemberLogin(
        @RequestBody @Valid GetLoginRequestDTO loginRequest) {
        return ApiResponse.success(memberAuthService.getMemberLogin(loginRequest));
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity getMemberLogout() {
        memberAuthService.getMemberLogout();
        return ResponseEntity.ok().body("로그아웃 완료");
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/refresh/{refreshToken}")
    public ApiResponse<GetNewTokenResponseDTO> getTokenRefreshed(
        @PathVariable("refreshToken") String refreshToken) {
        return ApiResponse.success(memberAuthService.getNewToken(refreshToken));
    }


    @Operation(summary = "비밀번호 찾기 (로그인 안한 상태) - 인증코드 요청")
    @PostMapping("/password/otp")
    public ApiResponse<CreateOtpResponseDTO> createOTP(
        @RequestBody @Valid CreateOtpRequestDTO postOtpRequestDTO) {
        return ApiResponse.success(memberAuthService.createOtp(postOtpRequestDTO));
    }

    @Operation(summary = "비밀번호 찾기 (로그인 안한 상태) - 인증코드 입력")
    @PostMapping("/password/otp/verify")
    public ApiResponse<PostVerifyOtpResponseDTO> verifyOTP(
        @RequestBody @Valid PostVerifyOtpRequestDTO postVerifyOtpRequestDTO) {
        return ApiResponse.success(memberAuthService.postVerifyOtp(postVerifyOtpRequestDTO));
    }

    @Operation(summary = "비밀번호 변경")
    @PatchMapping("/password")
    public ApiResponse<UpdatePasswordResponseDTO> updatePassword(
        @RequestBody @Valid UpdatePasswordRequestDTO updatePasswordRequestDTO) {
        return ApiResponse.success(memberAuthService.updatePassword(updatePasswordRequestDTO));
    }

}
