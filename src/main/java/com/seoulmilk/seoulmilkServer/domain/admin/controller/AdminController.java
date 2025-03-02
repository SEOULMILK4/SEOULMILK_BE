package com.seoulmilk.seoulmilkServer.domain.admin.controller;

import com.seoulmilk.seoulmilkServer.domain.admin.dto.GetAgencyListResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.GetEmployeeListResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.GetOneEmployeeResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.PostAdminLoginRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.PostAdminLoginResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.service.AdminService;
import com.seoulmilk.seoulmilkServer.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "[관리자 API]")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "관리자 로그인")
    @PostMapping("/login")
    public ApiResponse<PostAdminLoginResponseDTO> getMemberRegister(
        @RequestBody @Valid PostAdminLoginRequestDTO registerDTO) {
        return ApiResponse.success(adminService.postAdminLogin(registerDTO));
    }

    @Operation(summary = "관리자 로그아웃")
    @PostMapping("/logout")
    public ResponseEntity getAdminLogout() {
        adminService.getAdminLogout();
        return ResponseEntity.ok().body("관리자 로그아웃 완료");
    }

    @Operation(summary = "사원 목록 조회")
    @GetMapping("/employee")
    public ApiResponse<List<GetEmployeeListResponseDTO>> getEmployeeList() {
        return ApiResponse.success(adminService.getEmployeeList());
    }

    @Operation(summary = "개별 사원 조회")
    @GetMapping("/employee/{employeeId}")
    public ApiResponse<GetOneEmployeeResponseDTO> getOneEmployee(
        @PathVariable("employeeId") Long employeeId) {
        return ApiResponse.success(adminService.getOneEmployee(employeeId));
    }

    @Operation(summary = "대리점 목록 조회")
    @GetMapping("/agency")
    public ApiResponse<List<GetAgencyListResponseDTO>> getAgencyList() {
        return ApiResponse.success(adminService.getAgencyList());
    }

    // 사원, 대리점 등록


}
