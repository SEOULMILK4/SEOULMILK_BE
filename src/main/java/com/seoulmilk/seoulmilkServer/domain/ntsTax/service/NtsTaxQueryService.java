package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.IsSuccess;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.Status;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.ModifyNtsTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.ModifyNtsTaxResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.*;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;

public interface NtsTaxQueryService {

    GetNtsTaxListResponseDTO getNtsTax(Agency agency, Long ntsTaxId); // 세금 계산서 단건 조회

    GetNtsTaxListResponseDTO.NtsTaxListResponseDTO getNtsTaxList(Agency agency, Integer page,
        IsSuccess isSuccess); // 세금 계산서 목록 조회

    Page<NtsTax> searchNtsTaxList(Agency agency, Integer page, LocalDate startDate,
        LocalDate endDate, List<String> ipNameList); // 세금 계산서 통합 조회 - 조건 기준 탐색

    GetOneNtsTaxResponseDTO getOneNtsTaxInfo(Long ntsTaxId, Member member);

    ModifyNtsTaxResponseDTO modifyOneNtsTax(Long ntsTaxId, ModifyNtsTaxRequestDTO requestDTO,
        Member member);

    OcrTaxInvoiceResponseDTO revalidateOneNtsTax(Long ntsTaxId, Member member);

    GetHometaxResponseDTO.GetHometaxListResponseDTO getHometaxList(Member member, Integer page, Status status); // 본사 - 세금 계산서 이번 달 내역 조회

    GetHometaxResponseDTO.GetHometaxListResponseDTO getHometaxHistory(Member member, Integer page, Status status); // 본사 - 세금 계산서 전체 내역 통합 조회

    Page<NtsTax> searchHometaxList(Member member, Integer page, LocalDate startDate, LocalDate endDate, List<String> suNameList, List<String> ipNameList); // 세금 계산서 진위 여부 검증 후, 검색

    List<GetCsvResponseDTO> getHometaxCsv(Member member, LocalDate startDate, LocalDate endDate, List<String> suNameList, List<String> ipNameList, Status status); // csv 추출
}