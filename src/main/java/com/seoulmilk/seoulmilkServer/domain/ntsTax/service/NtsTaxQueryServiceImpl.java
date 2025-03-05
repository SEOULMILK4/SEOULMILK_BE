package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.agency.repository.AgencyRepository;
import com.seoulmilk.seoulmilkServer.domain.agency.service.AgencyAuthService;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.repository.NtsTaxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NtsTaxQueryServiceImpl implements NtsTaxQueryService {

    private final NtsTaxRepository ntsTaxRepository;
    private final AgencyAuthService agencyAuthService;

    @Override
    @Transactional(readOnly = true)
    public Page<NtsTax> getNtsTaxList(Agency agency, Integer page) {
        Pageable pageable = PageRequest.of(page, 13);

        Page<NtsTax> ntsTaxPage = ntsTaxRepository.findAll(pageable);

        return ntsTaxPage;
    }
}
