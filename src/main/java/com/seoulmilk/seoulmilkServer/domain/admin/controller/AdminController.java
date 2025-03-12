package com.seoulmilk.seoulmilkServer.domain.admin.controller;


import com.seoulmilk.seoulmilkServer.domain.admin.domain.Admin;
import com.seoulmilk.seoulmilkServer.domain.admin.service.AdminAuthService;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.Status;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.DeleteNtsTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.GetCsvRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetCsvResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetNtsTaxListResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.service.NtsTaxCommandService;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.service.NtsTaxQueryService;
import com.seoulmilk.seoulmilkServer.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/")
@Tag(name = "[관리자 API]")
public class AdminController {

    private final NtsTaxQueryService ntsTaxQueryService;
    private final NtsTaxCommandService ntsTaxCommandService;
    private final AdminAuthService adminAuthService;

    @Operation(summary = "세금 계산서 통합 조회")
    @GetMapping("/nts-tax")
    public ApiResponse<GetNtsTaxListResponseDTO.SearchNtsTaxListByAdminResponseDTO> getNtsTaxList(
        @RequestParam(name = "page") Integer page,
        @RequestParam(name = "status", required = false) Status status) {

        Admin admin = adminAuthService.getCurrentAdmin();

        return ApiResponse.success(ntsTaxQueryService.getNtsTaxListByStatusByAdmin(page, status));

    }

    @Operation(summary = "세금계산서 통합 조회 - 검색")
    @GetMapping("/nts-tax/search")
    public ApiResponse<GetNtsTaxListResponseDTO.SearchNtsTaxListByAdminResponseDTO> getNtsTaxListByAdmin(
        @RequestParam(name = "page") Integer page,
        @RequestParam(name = "status", required = false) Status status,
        @RequestParam(name = "startAt", required = false) LocalDate startDate,
        @RequestParam(name = "endAt", required = false) LocalDate endDate,
        @RequestParam(name = "suNameList", required = false) List<String> suNameList,
        @RequestParam(name = "ipNameList", required = false) List<String> ipNameList) {

        Admin admin = adminAuthService.getCurrentAdmin();

        return ApiResponse.success(ntsTaxQueryService.getNtsTaxListByAdmin(page, status, startDate,
            endDate, suNameList, ipNameList));

    }

    @Operation(summary = "세금 계산서 csv 추출 - 조건 설정시")
    @GetMapping("/nts-tax/csv")
    public ApiResponse<List<GetCsvResponseDTO>> getHometaxCsv(
        @RequestParam(name = "startAt", required = false) LocalDate startDate,
        @RequestParam(name = "endAt", required = false) LocalDate endDate,
        @RequestParam(name = "suNameList", required = false) List<String> suNameList,
        @RequestParam(name = "ipNameList", required = false) List<String> ipNameList,
        @RequestParam(name = "status", required = false) Status status) {

        Admin admin = adminAuthService.getCurrentAdmin();

        return ApiResponse.success(
            ntsTaxQueryService.getHometaxCsvByAdmin(admin, startDate, endDate, suNameList,
                ipNameList, status));
    }

    @Operation(summary = "세금 계산서 csv 추출 - 선택한 id만")
    @PostMapping("/nts-tax/csv")
    public ApiResponse<List<GetCsvResponseDTO>> getHometaxCsvByList(@RequestBody GetCsvRequestDTO requestDTO){

        Admin admin = adminAuthService.getCurrentAdmin();

        return ApiResponse.success(
            ntsTaxQueryService.getHometaxCsvByIdList(requestDTO));
    }

    @Operation(summary = "세금 계산서 삭제")
    @DeleteMapping("/nts-tax")
    public ApiResponse<String> deleteNtsTaxByAdmin(@RequestBody DeleteNtsTaxRequestDTO request) {

        Admin admin = adminAuthService.getCurrentAdmin();

        ntsTaxCommandService.deleteAdminNtsTaxList(request);

        return ApiResponse.success("세금계산서 삭제가 완료되었습니다.");
    }
}
