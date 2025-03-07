package com.seoulmilk.seoulmilkServer.domain.admin.controller;

import com.seoulmilk.seoulmilkServer.domain.admin.dto.agency.GetAgencyResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.agency.InviteAgenciesRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.agency.PostAgencyRegisterRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.agency.PostAgencyRegisterResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.agency.UpdateAgencyRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.agency.UpdateAgencyResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.service.AdminAgencyService;
import com.seoulmilk.seoulmilkServer.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "[관리자 API - 대리점 관련]")
public class AdminAgencyController {

    private final AdminAgencyService adminAgencyService;

    @Operation(summary = "대리점 목록 조회")
    @GetMapping("/agency")
    public ApiResponse<List<GetAgencyResponseDTO>> getAgencyList() {
        return ApiResponse.success(adminAgencyService.getAgencyList());
    }

    @Operation(summary = "개별 대리점 조회")
    @GetMapping("/agency/{agencyId}")
    public ApiResponse<GetAgencyResponseDTO> getOneAgency(
        @PathVariable("agencyId") Long agencyId) {
        return ApiResponse.success(adminAgencyService.getOneAgency(agencyId));
    }

    @Operation(summary = "개별 대리점 이메일 수정")
    @PatchMapping("/agency/{agencyId}")
    public ApiResponse<UpdateAgencyResponseDTO> updateAgencyInfo(
        @RequestBody @Valid UpdateAgencyRequestDTO requestDTO) {
        return ApiResponse.success(adminAgencyService.updateAgencyInfo(requestDTO));
    }

    // 대리점 등록
    @Operation(summary = "대리점 신규 등록")
    @PostMapping("/agency/register-agency")
    public ApiResponse<PostAgencyRegisterResponseDTO> registerOneAgency(
        @RequestBody @Valid PostAgencyRegisterRequestDTO requestDTO) {
        return ApiResponse.success(adminAgencyService.postAgencyRegister(requestDTO));
    }

    @Operation(summary = "대리점 일괄 등록")
    @PostMapping("/agency/register-agencies")
    public ApiResponse<List<PostAgencyRegisterResponseDTO>> registerAgencies(
        @RequestBody @Valid List<PostAgencyRegisterRequestDTO> requestDTO) {
        return ApiResponse.success(adminAgencyService.postAgenciesRegister(requestDTO));
    }

    @Operation(summary = "대리점 초대 메일 발송")
    @PostMapping("/agency/invite")
    public ResponseEntity inviteAgencies(
        @RequestBody @Valid InviteAgenciesRequestDTO requestDTO) {
        adminAgencyService.inviteAgencies(requestDTO);
        return ResponseEntity.ok("초대 메일 발송 완료");
    }

}
