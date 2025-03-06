package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.repository.NtsTaxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NtsTaxQueryServiceImpl implements NtsTaxQueryService {

    private final NtsTaxRepository ntsTaxRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<NtsTax> getNtsTaxList(Agency agency, Integer page) {
        Pageable pageable = PageRequest.of(page, 13);

        Page<NtsTax> ntsTaxPage = ntsTaxRepository.findAll(pageable);

        return ntsTaxPage;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NtsTax> searchNtsTaxList(Agency agency, Integer page, LocalDate startDate, LocalDate endDate, List<String> ipNameList) {
        Pageable pageable = PageRequest.of(page, 13);

        return ntsTaxRepository.searchNtsTaxList(agency, pageable, startDate, endDate, ipNameList);
    }
}
