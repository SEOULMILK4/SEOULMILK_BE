package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.Status;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface NtsTaxQueryService {
    Page<NtsTax> getNtsTaxList(Agency agency, Integer page); // 세금 계산서 목록 조회
    Page<NtsTax> searchNtsTaxList(Agency agency, Integer page, LocalDate startDate, LocalDate endDate, List<String> ipNameList); // 세금 계산서 통합 조회 - 조건 기준 탐색
    // 세금 계산서 진위 여부 검증 후, 상태 및 월 별 조회
    Page<NtsTax> searchHometaxList(Member member, Integer page, LocalDate startDate, LocalDate endDate, List<String> suNameList, List<String> ipNameList); // 세금 계산서 진위 여부 검증 후, 검색
}