package com.seoulmilk.seoulmilkServer.domain.agency.controller;

import com.seoulmilk.seoulmilkServer.domain.agency.dto.GetAgencyLoginRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.GetAgencyLoginResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.PostAgencyRegisterRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.dto.PostAgencyRegisterResponseDTO;
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
@Tag(name = "[대리점 API]")
public class AgencyController {

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


}
