package com.seoulmilk.seoulmilkServer.domain.admin.controller;

import com.seoulmilk.seoulmilkServer.domain.admin.dto.agency.GetEmployeeWithAgencyResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.employee.GetOneEmployeeResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.employee.PostEmployeeRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.employee.PostEmployeeResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.service.AdminEmployeeService;
import com.seoulmilk.seoulmilkServer.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/employee")
@Tag(name = "[관리자 API - 사원 관련]")
public class AdminEmployeeController {

    private final AdminEmployeeService adminEmployeeService;

    @Operation(summary = "사원 목록 조회")
    @GetMapping("")
    public ApiResponse<List<GetEmployeeWithAgencyResponseDTO>> getEmployeeList() {
        return ApiResponse.success(adminEmployeeService.getEmployeeList());
    }

    @Operation(summary = "개별 사원 조회")
    @GetMapping("/{employeeId}")
    public ApiResponse<GetOneEmployeeResponseDTO> getOneEmployee(
        @PathVariable("employeeId") Long employeeId) {
        return ApiResponse.success(adminEmployeeService.getOneEmployee(employeeId));
    }

    // 사원 등록
    @Operation(summary = "사원 신규 등록")
    @PostMapping("/register-employee")
    public ApiResponse<PostEmployeeResponseDTO> registerOneEmployee(
        @RequestBody @Valid PostEmployeeRequestDTO requestDTO) {
        return ApiResponse.success(adminEmployeeService.postEmployeeRegister(requestDTO));
    }

    @Operation(summary = "사원 일괄 등록")
    @PostMapping("/register-employees")
    public ApiResponse<List<PostEmployeeResponseDTO>> registerAgencies(
        @RequestBody @Valid List<PostEmployeeRequestDTO> requestDTO) {
        return ApiResponse.success(adminEmployeeService.postEmployeesRegister(requestDTO));
    }

}
