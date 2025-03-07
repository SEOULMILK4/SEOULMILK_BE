package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.SubmitNtxTaxRequestDTO;
import java.util.List;

public interface NtxTaxMappingService {

    void submitNtxTax(SubmitNtxTaxRequestDTO request);

}
