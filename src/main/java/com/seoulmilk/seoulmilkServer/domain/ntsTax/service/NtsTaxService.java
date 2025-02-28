package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetOcrResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NtsTaxService {
    List<GetOcrResponseDTO> ocrTestResponse(List<MultipartFile> files);
}