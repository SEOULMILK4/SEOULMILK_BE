package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.UpdateNtsTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.UpdateNtsTaxResponseDTO;

public interface NtsTaxCommandService {
    UpdateNtsTaxResponseDTO updateNtsTax(Member member, Long ntsTaxId, UpdateNtsTaxRequestDTO request); // 세금계산서 수정
    void deleteNtsTax(Member member, Long ntsTaxId); // 세금계산서 삭제
}