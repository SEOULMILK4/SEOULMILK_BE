package com.seoulmilk.seoulmilkServer.domain.ntsTax.controller;

import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetOcrResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.service.NtsTaxService;
import com.seoulmilk.seoulmilkServer.global.common.ApiResponse;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "[세금 계산서 및 OCR]")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OcrController {

    private final NtsTaxService ntsTaxService;

    @Operation(summary = "세금 계산서 OCR")
    @PostMapping(value = "/nts-tax", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<List<GetOcrResponseDTO>> getOcrTest(//@AuthenticationPrincipal Long userId,
                                                           @RequestParam("files") List<MultipartFile> files) {

        if (files.isEmpty()) {
            throw new BusinessException(ErrorCode.NTS_TAX_NOT_UPLOAD);
        }
        return ApiResponse.success(ntsTaxService.ocrTestResponse(files));
    }
}