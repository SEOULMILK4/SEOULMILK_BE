package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.UpdateNtsTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.UpdateNtsTaxResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.repository.NtsTaxRepository;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NtsTaxCommandServiceImpl implements NtsTaxCommandService {

    private final NtsTaxRepository ntsTaxRepository;

    @Override
    @Transactional
    public UpdateNtsTaxResponseDTO updateNtsTax(Agency agency, UpdateNtsTaxRequestDTO request) {
        NtsTax ntsTax = ntsTaxRepository.findById(request.getNtsTaxId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NTS_TAX_NOT_FOUND));

        // 담당 사원 & 대리점일 경우에만 수정 가능
        if (!ntsTax.getAgency().equals(agency)) {
            throw new BusinessException(ErrorCode.NTS_TAX_UPDATE_UNAUTHORIZED);
        }

        // 세금계산서 수정
        ntsTax.updateNtsTax(
                request.getIssueId(),
                request.getIssueDate(),
                request.getSuId(),
                request.getIpId(),
                request.getChargeTotal(),
                request.getTaxTotal(),
                request.getGrandTotal()
        );

        return UpdateNtsTaxResponseDTO.from(ntsTax);
    }

    @Override
    @Transactional
    public void deleteNtsTax(Agency agency, Long ntsTaxId) {
        NtsTax ntsTax = ntsTaxRepository.findById(ntsTaxId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NTS_TAX_NOT_FOUND));

        // 담당 사원 & 대리점일 경우에만 삭제 가능
        if (!ntsTax.getAgency().equals(agency)) {
            throw new BusinessException(ErrorCode.NTS_TAX_DELETE_UNAUTHORIZED);
        }
        ntsTaxRepository.delete(ntsTax);
    }
}
