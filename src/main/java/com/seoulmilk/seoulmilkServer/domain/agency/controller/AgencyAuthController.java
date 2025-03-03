package com.seoulmilk.seoulmilkServer.domain.agency.controller;

import com.seoulmilk.seoulmilkServer.domain.agency.dto.login.GetAgencyLoginRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.login.GetAgencyLoginResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.register.PostAgencyOTPRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.register.PostAgencyRegisterRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.register.PostAgencyRegisterResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.register.PostAgencyVerifyOTPRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.service.AgencyAuthService;
import com.seoulmilk.seoulmilkServer.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/agency")
@Tag(name = "[대리점 - 인증 API]")
public class AgencyAuthController {

    private final AgencyAuthService agencyAuthService;

    @Operation(summary = "대리점 회원가입")
    @PostMapping("/register")
    public ApiResponse<PostAgencyRegisterResponseDTO> postAgencyRegister(
        @RequestBody @Valid PostAgencyRegisterRequestDTO registerDTO) {
        return ApiResponse.success(agencyAuthService.postAgencyRegister(registerDTO));
    }

    @Operation(summary = "대리점 로그인")
    @PostMapping("/login")
    public ApiResponse<GetAgencyLoginResponseDTO> getAgencyLogin(
        @RequestBody @Valid GetAgencyLoginRequestDTO registerDTO) {
        return ApiResponse.success(agencyAuthService.getAgencyLogin(registerDTO));
    }

    @Operation(summary = "대리점 로그아웃")
    @PostMapping("/logout")
    public ResponseEntity getAgencyLogout() {
        agencyAuthService.getAgencyLogout();
        return ResponseEntity.ok().body("대리점 로그아웃 완료");
    }

    @Operation(summary = "대리점 회원가입 - 이메일 인증 인증코드 요청")
    @PostMapping("/register/otp")
    public ResponseEntity postAgencyCreateOtp(
        @RequestBody @Valid PostAgencyOTPRequestDTO requestDTO) {
        agencyAuthService.postAgencyCreateOtp(requestDTO);
        return ResponseEntity.ok().body("이메일로 인증번호가 발송되었습니다.");
    }

    @Operation(summary = "대리점 회원가입 - 이메일 인증 코드 입력")
    @PostMapping("/register/otp/verify/{optCode}")
    public  ResponseEntity verifyOTP(
        @RequestBody @Valid PostAgencyVerifyOTPRequestDTO requestDTO) {
        agencyAuthService.postVerifyOtp(requestDTO);
        return ResponseEntity.ok("인증 완료되었습니다.");
    }


}
