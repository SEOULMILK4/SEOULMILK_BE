package com.seoulmilk.seoulmilkServer.domain.member.service;

import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.ModifyNtsTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.ModifyNtsTaxResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetOneNtsTaxResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.OcrTaxInvoiceResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.service.NtsTaxQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberAuthService memberAuthService;
    private final NtsTaxQueryService ntsTaxQueryService;

    @Transactional(readOnly = true)
    public GetOneNtsTaxResponseDTO getOneNtsTax(Long ntsTaxId) {

        Member member = memberAuthService.getCurrentMember();

        return ntsTaxQueryService.getOneNtsTaxInfo(ntsTaxId, member);
    }


    @Transactional
    public ModifyNtsTaxResponseDTO modifyOneNtsTax(Long ntsTaxId,
        ModifyNtsTaxRequestDTO requestDTO) {

        Member member = memberAuthService.getCurrentMember();

        return ntsTaxQueryService.modifyOneNtsTax(ntsTaxId, requestDTO, member);
    }

    @Transactional
    public OcrTaxInvoiceResponseDTO revalidateOneNtsTax(Long ntsTaxId) {

        Member member = memberAuthService.getCurrentMember();

        return ntsTaxQueryService.revalidateOneNtsTax(ntsTaxId, member);
    }


}
