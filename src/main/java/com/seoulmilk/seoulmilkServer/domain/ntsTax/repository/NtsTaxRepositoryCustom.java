package com.seoulmilk.seoulmilkServer.domain.ntsTax.repository;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface NtsTaxRepositoryCustom {
    Page<NtsTax> searchNtsTaxList(Agency agency, Pageable pageable, LocalDate startDate, LocalDate endDate, List<String> ipNameList); // 세금계산서 통합 조회
    Page<NtsTax> searchHometaxList(Member member, Pageable pageable, LocalDate startDate, LocalDate endDate, List<String> suNameList, List<String> ipNameList); // 세금 계산서 진위 여부 검증 후, 검색
    List<NtsTax> findAllById(List<Long> ntsTaxList);
}
