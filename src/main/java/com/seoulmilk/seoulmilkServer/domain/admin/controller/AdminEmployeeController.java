package com.seoulmilk.seoulmilkServer.domain.admin.controller;

import com.seoulmilk.seoulmilkServer.domain.admin.dto.agency.GetEmployeeListResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.agency.GetEmployeeWithAgencyResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.employee.GetOneAgencyByEmployeeResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.employee.GetOneEmployeeResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.employee.PostAssignAgeciesRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.employee.PostEmployeeRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.employee.PostEmployeeResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.service.AdminEmployeeService;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/employee")
@Tag(name = "[관리자 API - 사원 관련]")
public class AdminEmployeeController {

    private final AdminEmployeeService adminEmployeeService;

    @Operation(summary = "사원 목록 조회")
    @GetMapping("")
    public ApiResponse<GetEmployeeListResponseDTO> getEmployeeList(
        @RequestParam(name = "page") Integer page) {
        Page<GetEmployeeWithAgencyResponseDTO> members = adminEmployeeService.getEmployeeList(page);

        return ApiResponse.success(GetEmployeeListResponseDTO.from(members));
    }

    @Operation(summary = "개별 사원 조회")
    @GetMapping("/{employeeId}")
    public ApiResponse<GetOneEmployeeResponseDTO> getOneEmployee(
        @PathVariable("employeeId") Long employeeId) {
        return ApiResponse.success(adminEmployeeService.getOneEmployee(employeeId));
    }

    @Operation(summary = "개별 사원 - 담당 가능 대리점 조회")
    @GetMapping("/{employeeId}/agency")
    public ApiResponse<List<GetOneAgencyByEmployeeResponseDTO>> getPossibleAgencyList(
        @PathVariable("employeeId") Long employeeId) {
        return ApiResponse.success(adminEmployeeService.getPossibleAgencyList(employeeId));
    }

    @Operation(summary = "개별 사원 - 담당 대리점 배정")
       @PostMapping("/{employeeId}/agency")
       public ApiResponse<List<GetOneAgencyByEmployeeResponseDTO>> assignAgencies(
           @PathVariable("employeeId") Long employeeId, @RequestBody @Valid
        PostAssignAgeciesRequestDTO requestDTO) {
           return ApiResponse.success(adminEmployeeService.assignAgencies(employeeId,requestDTO));
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
