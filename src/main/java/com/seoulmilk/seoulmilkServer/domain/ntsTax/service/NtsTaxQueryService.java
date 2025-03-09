package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.IsSuccess;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.ModifyNtsTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.ModifyNtsTaxResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetNtsTaxListResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetOneNtsTaxResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.OcrTaxInvoiceResponseDTO;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;

public interface NtsTaxQueryService {

    GetNtsTaxListResponseDTO.NtsTaxListResponseDTO getNtsTaxList(Agency agency, Integer page,
        IsSuccess isSuccess); // 세금 계산서 목록 조회

    Page<NtsTax> searchNtsTaxList(Agency agency, Integer page, LocalDate startDate,
        LocalDate endDate, List<String> ipNameList); // 세금 계산서 통합 조회 - 조건 기준 탐색

    GetOneNtsTaxResponseDTO getOneNtsTaxInfo(Long ntsTaxId, Member member);

    ModifyNtsTaxResponseDTO modifyOneNtsTax(Long ntsTaxId, ModifyNtsTaxRequestDTO requestDTO,
        Member member);

    OcrTaxInvoiceResponseDTO revalidateOneNtsTax(Long ntsTaxId, Member member);

    // 세금 계산서 진위 여부 검증 후, 상태 및 월 별 조회

    Page<NtsTax> searchHometaxList(Member member, Integer page, LocalDate startDate, LocalDate endDate, List<String> suNameList, List<String> ipNameList); // 세금 계산서 진위 여부 검증 후, 검색
}