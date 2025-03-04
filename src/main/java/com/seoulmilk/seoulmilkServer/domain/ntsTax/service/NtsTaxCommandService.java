package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.UpdateNtsTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.UpdateNtsTaxResponseDTO;

public interface NtsTaxCommandService {
    UpdateNtsTaxResponseDTO updateNtsTax(Agency agency, UpdateNtsTaxRequestDTO request); // 세금계산서 수정
    void deleteNtsTax(Agency agency, Long ntsTaxId); // 세금계산서 삭제
}