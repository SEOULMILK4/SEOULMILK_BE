package com.seoulmilk.seoulmilkServer.domain.auth.controller;

import com.seoulmilk.seoulmilkServer.domain.auth.dto.GetLoginRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.GetLoginResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.GetNewTokenResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.service.AgencyService;
import com.seoulmilk.seoulmilkServer.domain.auth.service.AuthService;
import com.seoulmilk.seoulmilkServer.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "[사원 - 로그인, 로그아웃, 토큰 재발급]")
public class AuthController {

    private final AuthService authService;
    private final AgencyService authRegisterService;

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ApiResponse<GetLoginResponseDTO> getMemberLogin(
        @RequestBody @Valid GetLoginRequestDTO loginRequest) {
        return ApiResponse.success(authService.getMemberLogin(loginRequest));
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity getMemberLogout() {
        authService.getMemberLogout();
        return ResponseEntity.ok().body("로그아웃 완료");
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/refresh/{refreshToken}")
    public ApiResponse<GetNewTokenResponseDTO> getTokenRefreshed(
        @PathVariable("refreshToken") String refreshToken) {
        return ApiResponse.success(authService.getNewToken(refreshToken));
    }



//    @Operation(summary = "비밀번호 해싱") // DB에 사용자 저장시 사용
//    @GetMapping("/api/getPassword")
//    public ResponseEntity getMemberLogin() {
//        authService.getHashedPassword();
//        return ResponseEntity.ok().body(null);
//    }


}
