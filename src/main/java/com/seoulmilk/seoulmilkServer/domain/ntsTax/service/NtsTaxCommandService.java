package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.DeleteNtsTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.UpdateNtsTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.ModifyNtsTaxResponseDTO;

public interface NtsTaxCommandService {
    ModifyNtsTaxResponseDTO updateNtsTax(Agency agency, Long ntsTaxId, UpdateNtsTaxRequestDTO request); // 세금계산서 수정
    void deleteAgencyNtsTaxList(Agency agency, DeleteNtsTaxRequestDTO request); // 세금계산서 목록 삭제 - 대리점
    void deleteEmployeeNtsTaxList(Member member, DeleteNtsTaxRequestDTO request);
    void deleteAgencyAllNtsTax(Agency agency);
    void deleteEmployeeAllNtsTax(Member member);
}