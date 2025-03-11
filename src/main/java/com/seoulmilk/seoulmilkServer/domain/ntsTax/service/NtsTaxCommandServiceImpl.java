package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.IsSuccess;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.Status;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.DeleteNtsTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.UpdateNtsTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.UpdateNtsTaxResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.repository.NtsTaxRepository;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import com.seoulmilk.seoulmilkServer.global.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NtsTaxCommandServiceImpl implements NtsTaxCommandService {

    private final NtsTaxRepository ntsTaxRepository;
    private final S3Service s3Service;

    @Override
    @Transactional
    public UpdateNtsTaxResponseDTO updateNtsTax(Agency agency, Long ntsTaxId,
        UpdateNtsTaxRequestDTO request) {
        NtsTax ntsTax = ntsTaxRepository.findById(ntsTaxId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NTS_TAX_NOT_FOUND));

        // 담당 대리점일 경우에만 수정 가능
        if (!ntsTax.getAgency().equals(agency)) {
            throw new BusinessException(ErrorCode.NTS_TAX_UPDATE_UNAUTHORIZED);
        }

        ntsTax.updateNtsTax(
            IsSuccess.SUCCESS,
            request.getIssueId(),
            request.getIssueDate(),
            request.getSuId(),
            request.getSuName(),
            request.getIpId(),
            request.getIpName(),
            request.getChargeTotal(),
            request.getTaxTotal(),
            request.getGrandTotal()
        );

        return UpdateNtsTaxResponseDTO.from(ntsTax);
    }

    @Override
    @Transactional
    public void deleteAgencyNtsTaxList(Agency agency, DeleteNtsTaxRequestDTO request) {
        List<NtsTax> ntsTaxeList = ntsTaxRepository.findAllById(request.getNtsTaxId());

        // 담당 대리점일 경우에만 삭제 가능
        for (NtsTax ntsTax : ntsTaxeList) {
            if (!ntsTax.getAgency().equals(agency)) {
                throw new BusinessException(ErrorCode.NTS_TAX_DELETE_UNAUTHORIZED);
            }
        }

        // S3에 저장된 파일명을 추출하여 리스트 생성
        List<String> fileNames = ntsTaxeList.stream()
            .map(NtsTax::getImageUrl)
            .collect(Collectors.toList());

        s3Service.deleteFileList(fileNames);
        ntsTaxRepository.deleteAll(ntsTaxeList);
    }

    @Override
    @Transactional
    public void deleteEmployeeNtsTaxList(Member member, DeleteNtsTaxRequestDTO request) {
        List<NtsTax> ntsTaxeList = ntsTaxRepository.findAllById(request.getNtsTaxId());

        // 담당 대리점일 경우에만 삭제 가능
        for (NtsTax ntsTax : ntsTaxeList) {
            if (!ntsTax.getMember().equals(member)) {
                throw new BusinessException(ErrorCode.NTS_TAX_DELETE_UNAUTHORIZED);
            }
        }

        // S3에 저장된 파일명을 추출하여 리스트 생성
        List<String> fileNames = ntsTaxeList.stream()
            .map(NtsTax::getImageUrl)
            .collect(Collectors.toList());

        s3Service.deleteFileList(fileNames);
        ntsTaxRepository.deleteAll(ntsTaxeList);
    }

    @Override
    @Transactional
    public void deleteAgencyAllNtsTax(Agency agency) {
        List<NtsTax> ntsTaxes = ntsTaxRepository.findByAgencyIdAndStatus(agency.getId(),
            Status.WAITING);

        if (ntsTaxes.isEmpty()) {
            throw new BusinessException(ErrorCode.NTS_TAX_NOT_FOUND);
        }

        // 이미지 파일 삭제
        for (NtsTax ntsTax : ntsTaxes) {
            s3Service.deleteFile(ntsTax.getImageUrl());
        }
        ntsTaxRepository.deleteAll(ntsTaxes);
    }

    @Override
    @Transactional
    public void deleteEmployeeAllNtsTax(Member member) {
        List<NtsTax> ntsTaxes = ntsTaxRepository.findByMemberIdAndStatusIn(
            member.getId(),
            List.of(Status.APPROVAL, Status.REJECTION)
        );

        if (ntsTaxes.isEmpty()) {
            throw new BusinessException(ErrorCode.NTS_TAX_NOT_FOUND);
        }

        // 이미지 파일 삭제
        for (NtsTax ntsTax : ntsTaxes) {
            s3Service.deleteFile(ntsTax.getImageUrl());
        }
        ntsTaxRepository.deleteAll(ntsTaxes);
    }

    @Override
    @Transactional
    public void deleteAdminNtsTaxList(DeleteNtsTaxRequestDTO request) {

        List<NtsTax> ntsTaxeList = ntsTaxRepository.findAllById(request.getNtsTaxId());

        List<String> fileNames = ntsTaxeList.stream()
               .map(NtsTax::getImageUrl)
               .collect(Collectors.toList());

           s3Service.deleteFileList(fileNames);
           ntsTaxRepository.deleteAll(ntsTaxeList);
    }
}
