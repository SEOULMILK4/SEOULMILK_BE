package com.seoulmilk.seoulmilkServer.domain.agency.controller;

import com.seoulmilk.seoulmilkServer.domain.agency.dto.etc.ChangeAgencyPasswordRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.login.CreateAgencyOtpRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.login.CreateAgencyOtpResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.login.GetAgencyLoginRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.login.GetAgencyLoginResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.login.UpdateAgencyPasswordRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.login.UpdateAgencyPasswordResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.register.PostAgencyOTPRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.register.PostAgencyRegisterRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.register.PostAgencyRegisterResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.register.PostAgencyVerifyOTPRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.service.AgencyAuthService;
import com.seoulmilk.seoulmilkServer.domain.member.dto.auth.GetNewTokenResponseDTO;
import com.seoulmilk.seoulmilkServer.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    @PostMapping("/register/otp/verify")
    public ResponseEntity verifyOTP(
        @RequestBody @Valid PostAgencyVerifyOTPRequestDTO requestDTO) {
        agencyAuthService.postVerifyOtp(requestDTO);
        return ResponseEntity.ok("인증 완료되었습니다.");
    }

    @Operation(summary = "비밀번호 찾기 (로그인 안한 상태) - 인증코드 요청")
    @PostMapping("/find-password/otp")
    public ApiResponse<CreateAgencyOtpResponseDTO> createOTP(
        @RequestBody @Valid CreateAgencyOtpRequestDTO requestDTO) {
        return ApiResponse.success(agencyAuthService.createOTP(requestDTO));
    }

    @Operation(summary = "비밀번호 찾기 (로그인 안한 상태) - 인증코드 입력")
    @PostMapping("/find-password/otp/verify")
    public ResponseEntity verifyPasswordOTP(
        @RequestBody @Valid PostAgencyVerifyOTPRequestDTO requestDTO) {
        agencyAuthService.postVerifyOtp(requestDTO);
        return ResponseEntity.ok("인증 완료되었습니다.");
    }

    @Operation(summary = "비밀번호 변경 (로그인 안한 상태) - 인증코드 입력후")
    @PatchMapping("/update-password")
    public ApiResponse<UpdateAgencyPasswordResponseDTO> updatePassword(
        @RequestBody @Valid UpdateAgencyPasswordRequestDTO requestDTO) {
        return ApiResponse.success(agencyAuthService.updatePassword(requestDTO));
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/refresh")
    public ApiResponse<GetNewTokenResponseDTO> getTokenRefreshed(
        @RequestHeader(value = "refreshToken", required = false) String refreshToken) {
        return ApiResponse.success(agencyAuthService.getNewToken(refreshToken));
    }


    @Operation(summary = "마이페이지 - 비번 변경")
    @PatchMapping("/my-page/update-password")
    public ResponseEntity updatePasswordMyPage(
        @RequestBody @Valid ChangeAgencyPasswordRequestDTO requestDTO) {
        agencyAuthService.changePassword(requestDTO);
        return ResponseEntity.ok("비밀번호 변경 완료");
    }

}
