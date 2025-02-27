package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetOcrTestResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NtsTaxService {
    List<GetOcrTestResponseDTO> ocrTestResponse(List<MultipartFile> files);
}