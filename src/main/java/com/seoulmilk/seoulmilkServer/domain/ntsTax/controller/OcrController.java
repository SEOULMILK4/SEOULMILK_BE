package com.seoulmilk.seoulmilkServer.domain.ntsTax.controller;

import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetOcrResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.ocr.ClovaOcr;
import com.seoulmilk.seoulmilkServer.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OcrController {

    private final ClovaOcr clovaOcr;

    @Value("${ocr.api.secret-key}")
    private static String secretKey;

    @Operation(summary = "세금 계산서 파일 OCR 처리")
    @PostMapping(value = "/nts-tax-ocr", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<GetOcrResponseDTO> getNtsTaxOcr () {

        return null;
    }
}
