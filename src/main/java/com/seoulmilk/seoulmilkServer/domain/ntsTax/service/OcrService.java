package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetOcrNtsTaxListResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OcrService {
    GetOcrNtsTaxListResponseDTO getOcrResponse(List<MultipartFile> files); // 세금계산서 업로드 + OCR
}
