package com.seoulmilk.seoulmilkServer.domain.auth.controller;

import com.seoulmilk.seoulmilkServer.domain.auth.dto.GetLoginRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.GetLoginResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.dto.GetNewTokenResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인")
    @PostMapping("/api/login")
    public ResponseEntity<GetLoginResponseDTO> getMemberLogin(
        @RequestBody @Valid GetLoginRequestDTO loginRequest) {
        return ResponseEntity.ok().body(authService.getMemberLogin(loginRequest));
    }

    // 로그아웃
    @Operation(summary = "로그아웃")
    @PostMapping("/api/logout")
    public ResponseEntity getMemberLogout() {
        authService.getMemberLogout();
        return ResponseEntity.ok().body(null);
    }

    // 토큰 재발급
    @Operation(summary = "토큰 재발급")
    @PostMapping("/api/refresh/{refreshToken}")
    public ResponseEntity<GetNewTokenResponseDTO> getTokenRefreshed(@PathVariable("refreshToken")String refreshToken) {
        return ResponseEntity.ok().body(authService.getNewToken(refreshToken));
    }

    // 비번 변경

    @Operation(summary = "비밀번호 해싱")
    @GetMapping("/api/getPassword")
    public ResponseEntity getMemberLogin() {
        authService.getHashedPassword();
        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "현재 로그인한 회원정보 조회")
    @GetMapping("/api/getMemberInfo")
    public ResponseEntity getMemberInfo() {
        Long id = authService.getCurrentMemberId();
        System.out.println("현재 로그인 멤버 id:" + id);
        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "토큰 검증")
    @GetMapping("/api/Info/{token}")
    public ResponseEntity valifyToken(@PathVariable("token") String token) {
        authService.valifyToken(token);
        return ResponseEntity.ok().body(null);
    }

}
