package com.seoulmilk.seoulmilkServer.domain.auth.controller;

import com.seoulmilk.seoulmilkServer.domain.auth.dto.UpdatePasswordRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.CreateOtpRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.PostVerifyOtpRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/password")
@Tag(name = "[인증 - 비밀번호 찾기 및 변경]")
public class AuthPasswordController {

    private final AuthService authService;

    @Operation(summary = "인증코드 요청")
    @PostMapping("/otp")
    public ResponseEntity verifyEmail(@RequestBody @Valid CreateOtpRequestDTO postOtpRequestDTO) {
        authService.createOtp(postOtpRequestDTO);
        return ResponseEntity.ok().body("인증번호가 전송 되었습니다.");
    }

    @Operation(summary = "인증코드 입력")
    @PostMapping("/otp/verify")
    public ResponseEntity verifyOTP(
        @RequestBody @Valid PostVerifyOtpRequestDTO postVerifyOtpRequestDTO
    ) {
        authService.postVerifyOtp(postVerifyOtpRequestDTO);
        return ResponseEntity.ok().body("인증번호가 인증되었습니다.");
    }

    @Operation(summary = "비밀번호 변경")
    @PatchMapping("")
    public ResponseEntity updatePassword(
        @RequestBody @Valid UpdatePasswordRequestDTO updatePasswordRequestDTO) {
        authService.updatePassword(updatePasswordRequestDTO);
        return ResponseEntity.ok().body("비밀번호 변경이 완료되었습니다.");
    }


}
