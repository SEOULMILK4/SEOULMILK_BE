package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.UpdateNtsTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetOcrResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.UpdateNtsTaxResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NtsTaxService {
    List<GetOcrResponseDTO> ocrTestResponse(Member member, List<MultipartFile> files); // 세금계산서 업로드 + OCR
    UpdateNtsTaxResponseDTO updateNtsTax(Member member, Long ntsTaxId, UpdateNtsTaxRequestDTO request); // 세금계산서 수정
}