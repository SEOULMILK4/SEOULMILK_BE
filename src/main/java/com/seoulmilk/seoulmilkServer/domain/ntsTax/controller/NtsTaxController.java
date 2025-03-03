package com.seoulmilk.seoulmilkServer.domain.ntsTax.controller;

import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.member.service.MemberAuthService;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.UpdateNtsTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetNtsTaxResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.UpdateNtsTaxResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.service.NtsTaxCommandService;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.service.OcrService;
import com.seoulmilk.seoulmilkServer.global.common.ApiResponse;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "[세금 계산서 및 OCR]")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NtsTaxController {

    private final MemberAuthService memberAuthService;
    private final NtsTaxCommandService ntsTaxService;
    private final OcrService ocrService;

    @Operation(summary = "세금 계산서 OCR")
    @PostMapping(value = "/nts-tax/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<List<GetNtsTaxResponseDTO>> getOcrTest(@RequestParam("files") List<MultipartFile> files) {

        if (files.isEmpty()) {
            throw new BusinessException(ErrorCode.NTS_TAX_NOT_UPLOAD);
        }
        Member member = memberAuthService.getCurrentMember();

        return ApiResponse.success(ocrService.getOcrResponse(member, files));
    }

    @Operation(summary = "세금 계산서 수정")
    @PutMapping("/nts-tax/{nts_tax_id}")
    public ApiResponse<UpdateNtsTaxResponseDTO> updateNtsTax(@RequestBody UpdateNtsTaxRequestDTO request,
                                                             @PathVariable("nts_tax_id") Long ntsTaxId) {

        Member member = memberAuthService.getCurrentMember();

        return ApiResponse.success(ntsTaxService.updateNtsTax(member, ntsTaxId, request));
    }

    @Operation(summary = "세금계산서 삭제")
    @DeleteMapping("/nts-tax/{nts_tax_id}")
    public ApiResponse<String> deleteNtsTax(@PathVariable("nts_tax_id") Long ntsTaxId) {
        Member member = memberAuthService.getCurrentMember();

        ntsTaxService.deleteNtsTax(member, ntsTaxId);

        return ApiResponse.success("Deletion successful");
    }
}