package com.seoulmilk.seoulmilkServer.domain.ntsTax.controller;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.agency.service.AgencyAuthService;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.member.service.MemberAuthService;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.Status;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.DeleteNtsTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.SubmitNtxTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.UpdateNtsTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetHometaxResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetNtsTaxListResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetOcrNtsTaxListResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.UpdateNtsTaxResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.service.HomeTaxService;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.service.NtsTaxCommandService;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.service.NtsTaxQueryService;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.service.NtxTaxMappingService;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.service.OcrService;
import com.seoulmilk.seoulmilkServer.global.common.ApiResponse;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "[세금 계산서 및 OCR]")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class NtsTaxController {

    private final MemberAuthService memberAuthService;
    private final AgencyAuthService agencyAuthService;
    private final NtsTaxCommandService ntsTaxCommandService;
    private final OcrService ocrService;
    private final NtsTaxQueryService ntsTaxQueryService;
    private final NtxTaxMappingService ntxTaxMappingService;

    @Operation(summary = "세금 계산서 OCR")
    @PostMapping(value = "/nts-tax/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<GetOcrNtsTaxListResponseDTO> getOcrTest(
        @RequestParam("files") List<MultipartFile> files) {

        if (files.isEmpty()) {
            throw new BusinessException(ErrorCode.NTS_TAX_NOT_UPLOAD);
        }

        return ApiResponse.success(ocrService.getOcrResponse(files));
    }

    @Operation(summary = "세금 계산서 수정")
    @PutMapping("/nts-tax/edit")
    public ApiResponse<UpdateNtsTaxResponseDTO> updateNtsTax(
        @RequestBody UpdateNtsTaxRequestDTO request) {

        Agency agency = agencyAuthService.getCurrentAgency();

        return ApiResponse.success(ntsTaxCommandService.updateNtsTax(agency, request));
    }

    @Operation(summary = "세금 계산서 목록 조회")
    @GetMapping("/nts-tax")
    public ApiResponse<GetNtsTaxListResponseDTO.NtsTaxListResponseDTO> getNtsTaxList(
        @RequestParam(name = "page") Integer page) {
        Agency agency = agencyAuthService.getCurrentAgency();

        Page<NtsTax> ntsTaxList = ntsTaxQueryService.getNtsTaxList(agency, page);

        return ApiResponse.success(GetNtsTaxListResponseDTO.from(ntsTaxList));
    }

    @Operation(summary = "세금 계산서 통합 조회 - 조건 설정 후 탐색")
    @GetMapping("/nts-tax/search")
    public ApiResponse<GetNtsTaxListResponseDTO.NtsTaxListResponseDTO> searchNtsTaxList(
        @RequestParam(name = "page") Integer page,
        @RequestParam(required = false) LocalDate startDate,
        @RequestParam(required = false) LocalDate endDate,
        @RequestParam(required = false) List<String> ipNameList) {
        Agency agency = agencyAuthService.getCurrentAgency();

        Page<NtsTax> ntsTaxList = ntsTaxQueryService.searchNtsTaxList(agency, page, startDate,
            endDate, ipNameList);

        return ApiResponse.success(GetNtsTaxListResponseDTO.from(ntsTaxList));
    }

    @Operation(summary = "세금계산서 단건 삭제")
    @DeleteMapping("/nts-tax/{nts_tax_id}")
    public ApiResponse<String> deleteNtsTax(@PathVariable("nts_tax_id") Long ntsTaxId) {
        Agency agency = agencyAuthService.getCurrentAgency();

        ntsTaxCommandService.deleteNtsTax(agency, ntsTaxId);

        return ApiResponse.success("Deletion successful");
    }

    @Operation(summary = "세금 계산서 페이지 내 다건 삭제")
    @DeleteMapping("/nts-tax/multiple")
    public ApiResponse<String> deleteNtsTaxList(@RequestBody List<DeleteNtsTaxRequestDTO> request) {
        Agency agency = agencyAuthService.getCurrentAgency();

        ntsTaxCommandService.deleteNtsTaxList(agency, request);

        return ApiResponse.success("NtsTaxList Deletion successful");
    }

    @Operation(summary = "세금계산서 제출")
    @PostMapping("/nts-tax/submit-hometax")
    public ResponseEntity submitNtsTaxList(@RequestBody SubmitNtxTaxRequestDTO request) {
        ntxTaxMappingService.submitNtxTax(request);
        return ResponseEntity.ok().body("세금계산서 제출 완료");
    }

//    @Operation(summary = "세금계산서 홈택스 진위 여부 확인 [단건]")
//    @PostMapping("/nts-tax/hometax")
//    public ApiResponse<OcrTaxInvoiceResponseDTO> getOneVerify(
//        @RequestBody OcrTaxInvoiceRequestDTO request) {
//        return ApiResponse.success(homeTaxService.verifyTaxInvoice(request));
//    }
//
//    @Operation(summary = "세금계산서 홈택스 진위 여부 확인 [다건]")
//    @PostMapping("/nts-tax/hometax/multiple")
//    public ApiResponse<List<OcrTaxInvoiceResponseDTO>> getMultipleVerify(
//        @RequestBody List<OcrTaxInvoiceRequestDTO> requests) {
//        return ApiResponse.success(homeTaxService.verifyMultipleTaxInvoice(requests));
//    }

    @Operation(summary = "세금 계산서 진위 여부 검증 후, 본사 측 검색")
    @GetMapping("/nts-tax/search-hometax")
    public ApiResponse<GetHometaxResponseDTO.GetHometaxListResponseDTO> searchHometaxList (@RequestParam(name = "page") Integer page,
                                                                                           @RequestParam(required = false) LocalDate startMonth,
                                                                                           @RequestParam(required = false) LocalDate endMonth,
                                                                                           @RequestParam(required = false) String suName,
                                                                                           @RequestParam(required = false) String ipName) {
        Member member = memberAuthService.getCurrentMember();

        Page<NtsTax> searchHometaxList = ntsTaxQueryService.searchHometaxList(member, page, startMonth, endMonth, suName, ipName);

        return ApiResponse.success(GetHometaxResponseDTO.from(searchHometaxList));
    }
}