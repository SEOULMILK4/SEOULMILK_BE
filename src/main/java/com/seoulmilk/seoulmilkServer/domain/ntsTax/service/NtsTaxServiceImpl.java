package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.seoulmilk.seoulmilkServer.domain.ntsTax.ocr.ClovaOcr;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.repository.NtsTaxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NtsTaxServiceImpl implements NtsTaxService {

    private final ClovaOcr clovaOcr;
    private final NtsTaxRepository ntsTaxRepository;
}
