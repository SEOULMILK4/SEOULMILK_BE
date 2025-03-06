package com.seoulmilk.seoulmilkServer.domain.ntsTax.ocr;

import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.OcrRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.OcrResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "clovaOcrClient", url = "${ocr.api.url}")
public interface ClovaOcrClient {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    OcrResponseDTO callApi(
            @RequestHeader("X-OCR-SECRET") String secretKey,
            @RequestBody OcrRequestDTO requestDTO
    );
}
