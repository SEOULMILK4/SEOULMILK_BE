package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.seoulmilk.seoulmilkServer.domain.admin.domain.Admin;
import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.IsSuccess;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.Status;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.ModifyNtsTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.ModifyNtsTaxResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetCsvResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetHometaxResponseDTO;
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

    GetNtsTaxListResponseDTO.SearchNtsTaxListByAdminResponseDTO getNtsTaxListByStatusByAdmin(
        Integer page,
        Status status);

    GetNtsTaxListResponseDTO.SearchNtsTaxListByAdminResponseDTO getNtsTaxListByAdmin(Integer page,
        Status status, LocalDate startDate,
        LocalDate endDate, List<String> suNameList, List<String> ipNameList);

    GetOneNtsTaxResponseDTO getOneNtsTaxInfo(Long ntsTaxId, Member member);

    ModifyNtsTaxResponseDTO modifyOneNtsTax(Long ntsTaxId, ModifyNtsTaxRequestDTO requestDTO,
        Member member);

    OcrTaxInvoiceResponseDTO revalidateOneNtsTax(Long ntsTaxId, Member member);

    GetHometaxResponseDTO.GetHometaxListResponseDTO getHometaxList(Member member, Integer page,
        Status status); // 본사 - 세금 계산서 이번 달 내역 조회

    GetHometaxResponseDTO.GetHometaxListResponseDTO getHometaxHistory(Member member, Integer page,
        Status status); // 본사 - 세금 계산서 전체 내역 통합 조회

    List<GetHometaxResponseDTO> searchHometaxList(Member member, LocalDate startDate,
        LocalDate endDate, List<String> suNameList,
        List<String> ipNameList); // 세금 계산서 진위 여부 검증 후, 검색

    List<GetCsvResponseDTO> getHometaxCsv(Member member, LocalDate startDate, LocalDate endDate,
        List<String> suNameList, List<String> ipNameList, Status status); // 본사 - csv 추출

    List<GetCsvResponseDTO> getHometaxCsvByAdmin(Admin admin, LocalDate startDate, LocalDate endDate,
                                                 List<String> suNameList, List<String> ipNameList, Status status); // 관리자 - csv 추출
}