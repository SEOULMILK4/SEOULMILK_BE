package com.seoulmilk.seoulmilkServer.domain.ntsTax.controller;

import com.seoulmilk.seoulmilkServer.domain.auth.service.AuthService;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.UpdateNtsTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetOcrResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.UpdateNtsTaxResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.service.NtsTaxService;
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

    private final AuthService authService;
    private final NtsTaxService ntsTaxService;

    @Operation(summary = "세금 계산서 OCR")
    @PostMapping(value = "/nts-tax", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<List<GetOcrResponseDTO>> getOcrTest(@RequestParam("files") List<MultipartFile> files) {

        if (files.isEmpty()) {
            throw new BusinessException(ErrorCode.NTS_TAX_NOT_UPLOAD);
        }
        Member member = authService.getCurrentMember();

        return ApiResponse.success(ntsTaxService.ocrTestResponse(member, files));
    }

    @Operation(summary = "세금 계산서 수정")
    @PutMapping("/nts-tax/{nts_tax_id}")
    public ApiResponse<UpdateNtsTaxResponseDTO> updateNtsTax(@RequestBody UpdateNtsTaxRequestDTO request,
                                                             @PathVariable("nts_tax_id") Long ntsTaxId) {

        Member member = authService.getCurrentMember();

        return ApiResponse.success(ntsTaxService.updateNtsTax(member, ntsTaxId, request));
    }
}