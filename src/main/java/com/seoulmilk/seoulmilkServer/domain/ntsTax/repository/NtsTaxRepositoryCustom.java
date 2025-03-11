package com.seoulmilk.seoulmilkServer.domain.ntsTax.repository;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.Status;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetCsvResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetHometaxResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface NtsTaxRepositoryCustom {
    Page<NtsTax> searchNtsTaxList(Agency agency, Pageable pageable, LocalDate startDate, LocalDate endDate, List<String> ipNameList); // 세금계산서 통합 조회
    GetHometaxResponseDTO.GetHometaxListResponseDTO searchHometaxList(Member member, Pageable pageable, LocalDate startDate, LocalDate endDate, List<String> suNameList, List<String> ipNameList); // 세금 계산서 진위 여부 검증 후, 검색
    Page<NtsTax> searchHometaxListByAdmin(Pageable pageable, Status status, LocalDate startDate, LocalDate endDate, List<String> suNameList, List<String> ipNameList);
    List<GetCsvResponseDTO> getHometaxCsv(Member member, LocalDate startDate, LocalDate endDate, List<String> suNameList, List<String> ipNameList, List<NtsTax> ntsTaxList); // 세금 계산서 csv 추출
    List<NtsTax> findAllById(List<Long> ntsTaxList);
}
