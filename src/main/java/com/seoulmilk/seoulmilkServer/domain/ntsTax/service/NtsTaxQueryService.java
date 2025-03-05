package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import org.springframework.data.domain.Page;

public interface NtsTaxQueryService {
    Page<NtsTax> getNtsTaxList(Agency agency, Integer page); // 세금 계산서 목록 조회
}
