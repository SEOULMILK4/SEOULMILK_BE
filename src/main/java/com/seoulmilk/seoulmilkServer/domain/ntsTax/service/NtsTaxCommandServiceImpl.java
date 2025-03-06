package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.DeleteNtsTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.UpdateNtsTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.UpdateNtsTaxResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.repository.NtsTaxRepository;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NtsTaxCommandServiceImpl implements NtsTaxCommandService {

    private final NtsTaxRepository ntsTaxRepository;

    @Override
    @Transactional
    public List<UpdateNtsTaxResponseDTO> updateNtsTax(Agency agency, List<UpdateNtsTaxRequestDTO> requestList) {
        List<Long> ntsTaxIds = requestList.stream()
                .map(UpdateNtsTaxRequestDTO::getNtsTaxId)
                .collect(Collectors.toList());

        List<NtsTax> ntsTaxeList = ntsTaxRepository.findAllById(ntsTaxIds);

        // 요청한 ID 개수와 DB 내 ID 개수가 다를 경우
        if (ntsTaxeList.size() != ntsTaxIds.size()) {
            throw new BusinessException(ErrorCode.NTS_TAX_NOT_FOUND);
        }
        List<UpdateNtsTaxResponseDTO> responseList = new ArrayList<>();

        for (NtsTax ntsTax : ntsTaxeList) {
            UpdateNtsTaxRequestDTO request = requestList.stream()
                    .filter(r -> r.getNtsTaxId().equals(ntsTax.getId()))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException(ErrorCode.NTS_TAX_NOT_FOUND));

            // 담당 대리점일 경우에만 수정 가능
            if (!ntsTax.getAgency().equals(agency)) {
                throw new BusinessException(ErrorCode.NTS_TAX_UPDATE_UNAUTHORIZED);
            }

            ntsTax.updateNtsTax(
                    request.getIssueId(),
                    request.getIssueDate(),
                    request.getSuId(),
                    request.getIpId(),
                    request.getChargeTotal(),
                    request.getTaxTotal(),
                    request.getGrandTotal()
            );
            responseList.add(UpdateNtsTaxResponseDTO.from(ntsTax));
        }
        return responseList;
    }

    @Override
    @Transactional
    public void deleteNtsTax(Agency agency, Long ntsTaxId) {
        NtsTax ntsTax = ntsTaxRepository.findById(ntsTaxId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NTS_TAX_NOT_FOUND));

        // 담당 대리점일 경우에만 삭제 가능
        if (!ntsTax.getAgency().equals(agency)) {
            throw new BusinessException(ErrorCode.NTS_TAX_DELETE_UNAUTHORIZED);
        }
        ntsTaxRepository.delete(ntsTax);
    }

    @Override
    @Transactional
    public void deleteNtsTaxList(Agency agency, List<DeleteNtsTaxRequestDTO> request) {

        List<Long> ntsTaxIds = request.stream()
                .map(DeleteNtsTaxRequestDTO::getNtsTaxId)
                .collect(Collectors.toList());

        List<NtsTax> ntsTaxeList = ntsTaxRepository.findAllById(ntsTaxIds);

        // 요청한 ID 개수와 DB 내 ID 개수가 다를 경우
        if (ntsTaxeList.size() != ntsTaxIds.size()) {
            throw new BusinessException(ErrorCode.NTS_TAX_NOT_FOUND);
        }

        // 담당 대리점일 경우에만 삭제 가능
        for (NtsTax ntsTax : ntsTaxeList) {
            if (!ntsTax.getAgency().equals(agency)) {
                throw new BusinessException(ErrorCode.NTS_TAX_DELETE_UNAUTHORIZED);
            }
        }
        ntsTaxRepository.deleteAll(ntsTaxeList);
    }
}
