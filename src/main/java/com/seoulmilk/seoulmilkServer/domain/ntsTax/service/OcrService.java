package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetNtsTaxResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OcrService {
    List<GetNtsTaxResponseDTO> getOcrResponse(Agency agency, List<MultipartFile> files); // 세금계산서 업로드 + OCR
}
