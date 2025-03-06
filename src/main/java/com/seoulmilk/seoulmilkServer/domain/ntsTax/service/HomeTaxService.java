package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.OcrTaxInvoiceRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.OcrTaxInvoiceResponseDTO;
import java.util.List;

public interface HomeTaxService {

    OcrTaxInvoiceResponseDTO verifyTaxInvoice(OcrTaxInvoiceRequestDTO request);

    List<OcrTaxInvoiceResponseDTO> verifyMultipleTaxInvoice(
        List<OcrTaxInvoiceRequestDTO> requests);

    OcrTaxInvoiceResponseDTO processTaxInvoiceVerification(OcrTaxInvoiceRequestDTO request,
        String accessToken);
}
