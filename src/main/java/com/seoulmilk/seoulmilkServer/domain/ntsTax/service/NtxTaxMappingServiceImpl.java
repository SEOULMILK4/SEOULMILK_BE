package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.agency.service.AgencyAuthService;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.Status;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.OcrTaxInvoiceRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.SubmitNtxTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.OcrTaxInvoiceResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.repository.NtsTaxRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NtxTaxMappingServiceImpl implements NtxTaxMappingService {

    private final NtsTaxRepository ntsTaxRepository;
    private final AgencyAuthService agencyAuthService;
    private final HomeTaxService homeTaxService;

    public void submitNtxTax(SubmitNtxTaxRequestDTO request) {

        Agency agency = agencyAuthService.getCurrentAgency();

        List<NtsTax> ntxTaxes = ntsTaxRepository.findAllById(request.getIdList());

        List<OcrTaxInvoiceRequestDTO> ntxTaxRequest = ntxTaxes.stream()
            .map(OcrTaxInvoiceRequestDTO::from)
            .toList();

        List<OcrTaxInvoiceResponseDTO> results = homeTaxService.verifyMultipleTaxInvoice(
            ntxTaxRequest);

        Set<Long> approvedIds = results.stream()
            .filter(dto -> "1".equals(dto.getResAuthenticity()))
            .map(OcrTaxInvoiceResponseDTO::getId)
            .collect(Collectors.toSet());

        ntxTaxes.forEach(ntxTax -> {
            Status status =
                approvedIds.contains(ntxTax.getId()) ? Status.APPROVAL : Status.REJECTION;
            ntxTax.updateStatus(status);
        });

        ntsTaxRepository.saveAll(ntxTaxes);
    }
}
