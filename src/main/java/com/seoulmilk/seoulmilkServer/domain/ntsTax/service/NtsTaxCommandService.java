package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.DeleteNtsTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.UpdateNtsTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.UpdateNtsTaxResponseDTO;

import java.util.List;

public interface NtsTaxCommandService {
    UpdateNtsTaxResponseDTO updateNtsTax(Agency agency, UpdateNtsTaxRequestDTO request); // 세금계산서 수정
    void deleteNtsTax(Agency agency, Long ntsTaxId); // 세금계산서 단건 삭제
    void deleteNtsTaxList(Agency agency, List<DeleteNtsTaxRequestDTO> request); // 세금계산서 목록 삭제
}