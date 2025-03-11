package com.seoulmilk.seoulmilkServer.domain.admin.controller;


import com.seoulmilk.seoulmilkServer.domain.admin.domain.Admin;
import com.seoulmilk.seoulmilkServer.domain.admin.service.AdminAuthService;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.Status;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetCsvResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetNtsTaxListResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.service.NtsTaxQueryService;
import com.seoulmilk.seoulmilkServer.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/")
@Tag(name = "[관리자 API]")
public class AdminController {

    private final NtsTaxQueryService ntsTaxQueryService;
    private final AdminAuthService adminAuthService;

    @Operation(summary = "세금 계산서 통합 조회")
    @GetMapping("/nts-tax")
    public ApiResponse<GetNtsTaxListResponseDTO.SearchNtsTaxListResponseDTO> getNtsTaxList(
        @RequestParam(name = "page") Integer page,
        @RequestParam(name = "status", required = false) Status status) {

        Admin admin = adminAuthService.getCurrentAdmin();

        Page<NtsTax> ntsTaxList = ntsTaxQueryService.getNtsTaxListByStatusByAdmin(page, status);

        return ApiResponse.success(GetNtsTaxListResponseDTO.from(ntsTaxList));
    }

    @Operation(summary = "세금계산서 통합 조회 - 검색")
    @GetMapping("/nts-tax/search")
    public ApiResponse<GetNtsTaxListResponseDTO.SearchNtsTaxListResponseDTO> getNtsTaxListByAdmin(
        @RequestParam(name = "page") Integer page,
        @RequestParam(name = "status",required = false) Status status,
        @RequestParam(name = "startAt", required = false) LocalDate startDate,
        @RequestParam(name = "endAt", required = false) LocalDate endDate,
        @RequestParam(name = "suNameList", required = false) List<String> suNameList,
        @RequestParam(name = "ipNameList", required = false) List<String> ipNameList) {

        Admin admin = adminAuthService.getCurrentAdmin();

        Page<NtsTax> ntsTaxList = ntsTaxQueryService.getNtsTaxListByAdmin(page, status, startDate,
            endDate, suNameList, ipNameList);

        return ApiResponse.success(GetNtsTaxListResponseDTO.from(ntsTaxList));
    }

    @Operation(summary = "세금 계산서 csv 추출")
    @GetMapping("/nts-tax/csv")
    public ApiResponse<List<GetCsvResponseDTO>> getHometaxCsv(
            @RequestParam(required = false) LocalDate startMonth,
            @RequestParam(required = false) LocalDate endMonth,
            @RequestParam(required = false) List<String> suNameList,
            @RequestParam(required = false) List<String> ipNameList,
            @RequestParam(required = false) Status status) {

        Admin admin = adminAuthService.getCurrentAdmin();

        return ApiResponse.success(ntsTaxQueryService.getHometaxCsvByAdmin(admin, startMonth, endMonth, suNameList, ipNameList, status));
    }
}
