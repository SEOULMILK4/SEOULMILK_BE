package com.seoulmilk.seoulmilkServer.domain.ntsTax.controller;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.agency.service.AgencyAuthService;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.member.service.MemberAuthService;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.DeleteNtsTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.service.NtsTaxCommandService;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.service.NtxTaxMappingService;
import com.seoulmilk.seoulmilkServer.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "[사원, 대리점 공통 API]")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class NtsTaxController {

    private final MemberAuthService memberAuthService;
    private final AgencyAuthService agencyAuthService;
    private final NtsTaxCommandService ntsTaxCommandService;
    private final NtxTaxMappingService ntxTaxMappingService;

    @Operation(summary = "본사, 대리점 - 세금 계산서 단건 삭제")
    @DeleteMapping("/nts-tax/{nts_tax_id}")
    public ApiResponse<String> deleteNtsTax(@PathVariable("nts_tax_id") Long ntsTaxId) {
        Agency agency = agencyAuthService.getCurrentAgency();
        Member member = memberAuthService.getCurrentMember();

        ntsTaxCommandService.deleteNtsTax(agency, ntsTaxId);

        return ApiResponse.success("Deletion successful");
    }

    @Operation(summary = "본사, 대리점 - 세금 계산서 페이지 내 다건 삭제")
    @DeleteMapping("/nts-tax/multiple")
    public ApiResponse<String> deleteNtsTaxList(@RequestBody List<DeleteNtsTaxRequestDTO> request) {
        Agency agency = agencyAuthService.getCurrentAgency();

        ntsTaxCommandService.deleteNtsTaxList(agency, request);

        return ApiResponse.success("NtsTaxList Deletion successful");
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
}