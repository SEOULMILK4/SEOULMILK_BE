package com.seoulmilk.seoulmilkServer.domain.auth.controller;

import com.seoulmilk.seoulmilkServer.domain.auth.dto.CreateOtpRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.CreateOtpResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.PostVerifyOtpRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.PostVerifyOtpResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.UpdatePasswordRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.UpdatePasswordResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.service.AuthVerifyService;
import com.seoulmilk.seoulmilkServer.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/password")
@Tag(name = "[사원 - 비밀번호 찾기 및 변경]")
public class AuthPasswordController {

    private final AuthVerifyService authVerifyService;

    @Operation(summary = "인증코드 요청")
    @PostMapping("/otp")
    public ApiResponse<CreateOtpResponseDTO> createOTP(
        @RequestBody @Valid CreateOtpRequestDTO postOtpRequestDTO) {
        return ApiResponse.success(authVerifyService.createOtp(postOtpRequestDTO));
    }

    @Operation(summary = "인증코드 입력")
    @PostMapping("/otp/verify")
    public ApiResponse<PostVerifyOtpResponseDTO> verifyOTP(
        @RequestBody @Valid PostVerifyOtpRequestDTO postVerifyOtpRequestDTO) {
        return ApiResponse.success(authVerifyService.postVerifyOtp(postVerifyOtpRequestDTO));
    }

    @Operation(summary = "비밀번호 변경")
    @PatchMapping("")
    public ApiResponse<UpdatePasswordResponseDTO> updatePassword(
        @RequestBody @Valid UpdatePasswordRequestDTO updatePasswordRequestDTO) {
        return ApiResponse.success(authVerifyService.updatePassword(updatePasswordRequestDTO));
    }


}
