package com.seoulmilk.seoulmilkServer.domain.ntsTax.service;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.IsSuccess;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.Status;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.ModifyNtsTaxRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.ModifyNtsTaxResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.request.OcrTaxInvoiceRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetCsvResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetHometaxResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetNtsTaxListResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.GetOneNtsTaxResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.dto.response.OcrTaxInvoiceResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.repository.NtsTaxRepository;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NtsTaxQueryServiceImpl implements NtsTaxQueryService {

    private final NtsTaxRepository ntsTaxRepository;
    private final HomeTaxService homeTaxService;

    @Override
    @Transactional(readOnly = true)
    public GetNtsTaxListResponseDTO.NtsTaxListResponseDTO getNtsTaxList(Agency agency, Integer page,
        IsSuccess isSuccess) {
        Pageable pageable = PageRequest.of(page, 13, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<NtsTax> ntsTaxPage = ntsTaxRepository.findByAgencyIdAndIsSuccessAndStatus(
            agency.getId(),
            isSuccess, pageable, Status.WAITING);

        // 전체 성공, 실패 건 수 조회
        Long successCnt = ntsTaxRepository.countByAgencyIdAndIsSuccessAndStatus(agency.getId(),
            IsSuccess.SUCCESS, Status.WAITING);
        Long failedCnt = ntsTaxRepository.countByAgencyIdAndIsSuccessAndStatus(agency.getId(),
            IsSuccess.FAILED, Status.WAITING);

        return GetNtsTaxListResponseDTO.of(ntsTaxPage, successCnt, failedCnt);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NtsTax> getNtsTaxListByStatusByAdmin(Integer page,
        Status status) {
        Pageable pageable = PageRequest.of(page, 13, Sort.by(Sort.Direction.DESC, "createdAt"));

        if (status == null) {
            return ntsTaxRepository.findAll(pageable);
        } else {
            return ntsTaxRepository.findByStatus(status, pageable);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NtsTax> getNtsTaxListByAdmin(Integer page,
        Status status, LocalDate startDate, LocalDate endDate, List<String> suNameList,
        List<String> ipNameList) {

        Pageable pageable = PageRequest.of(page, 13, Sort.by(Sort.Direction.DESC, "createdAt"));

        return ntsTaxRepository.searchHometaxListByAdmin(pageable, status,
            startDate, endDate, suNameList, ipNameList);

    }


    @Override
    @Transactional(readOnly = true)
    public Page<NtsTax> searchNtsTaxList(Agency agency, Integer page, LocalDate startDate,
        LocalDate endDate, List<String> ipNameList) {
        Pageable pageable = PageRequest.of(page, 13);

        return ntsTaxRepository.searchNtsTaxList(agency, pageable, startDate, endDate, ipNameList);
    }

    @Override
    @Transactional(readOnly = true)
    public GetHometaxResponseDTO.GetHometaxListResponseDTO getHometaxList(Member member,
        Integer page, Status status) {
        if (status.equals(Status.WAITING)) {
            throw new BusinessException(ErrorCode.WAITING_NOT_SELECTED);
        }

        Pageable pageable = PageRequest.of(page, 13, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<NtsTax> ntsTaxPage = ntsTaxRepository.findByMemberAndStatusThisMonth(member, status,
            pageable);

        // 전체 일치, 불일치 건 수 조회
        Long successCnt = ntsTaxRepository.countByMemberAndStatusThisMonth(member, Status.APPROVAL);
        Long failedCnt = ntsTaxRepository.countByMemberAndStatusThisMonth(member, Status.REJECTION);

        return GetHometaxResponseDTO.of(ntsTaxPage, successCnt, failedCnt);
    }

    @Override
    @Transactional(readOnly = true)
    public GetHometaxResponseDTO.GetHometaxListResponseDTO getHometaxHistory(Member member,
        Integer page, Status status) {
        if (status != null && status.equals(Status.WAITING)) {
            throw new BusinessException(ErrorCode.WAITING_NOT_SELECTED);
        }

        Pageable pageable = PageRequest.of(page, 13, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<NtsTax> ntsTaxPage;

        if (status == null) {
            List<Status> validStatuses = Arrays.asList(Status.APPROVAL, Status.REJECTION);
            ntsTaxPage = ntsTaxRepository.findByMemberAndStatusIn(member, validStatuses, pageable);
        } else {
            ntsTaxPage = ntsTaxRepository.findByMemberAndStatus(member, status, pageable);
        }

        // 전체 일치, 불일치 건 수 조회
        Long successCnt = ntsTaxRepository.countByMemberIdAndStatus(member.getId(), Status.APPROVAL);
        Long failedCnt = ntsTaxRepository.countByMemberIdAndStatus(member.getId(), Status.REJECTION);

        return GetHometaxResponseDTO.of(ntsTaxPage, successCnt, failedCnt);
    }

    @Override
    @Transactional(readOnly = true)
    public GetHometaxResponseDTO.GetHometaxListResponseDTO searchHometaxList(Member member, Integer page, LocalDate startDate,
        LocalDate endDate, List<String> suNameList, List<String> ipNameList) {
        Pageable pageable = PageRequest.of(page, 13);

        return ntsTaxRepository.searchHometaxList(member, pageable, startDate, endDate, suNameList,
            ipNameList);
    }

    public GetOneNtsTaxResponseDTO getOneNtsTaxInfo(Long ntsTaxId, Member member) {

        NtsTax ntsTax = ntsTaxRepository.findById(ntsTaxId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NTS_TAX_NOT_FOUND));

        if (!ntsTax.getMember().equals(member)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        return GetOneNtsTaxResponseDTO.from(ntsTax);
    }

    @Override
    @Transactional
    public ModifyNtsTaxResponseDTO modifyOneNtsTax(Long ntsTaxId,
        ModifyNtsTaxRequestDTO requestDTO, Member member) {

        NtsTax ntsTax = ntsTaxRepository.findById(ntsTaxId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NTS_TAX_NOT_FOUND));

        if (!ntsTax.getMember().equals(member)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        ntsTax.modifyNtsTax(requestDTO.getSuName(), requestDTO.getIpName(), requestDTO.getIssueId(),
            requestDTO.getIssueDate(),
            requestDTO.getSuId(), requestDTO.getIpId(),
            requestDTO.getChargeTotal(), requestDTO.getTaxTotal(), requestDTO.getGrandTotal());

        return ModifyNtsTaxResponseDTO.from(ntsTax);
    }

    @Override
    @Transactional
    public OcrTaxInvoiceResponseDTO revalidateOneNtsTax(Long ntsTaxId, Member member) {

        NtsTax ntsTax = ntsTaxRepository.findById(ntsTaxId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NTS_TAX_NOT_FOUND));

        if (!ntsTax.getMember().equals(member)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        if (ntsTax.getStatus().equals(Status.APPROVAL)) {
            throw new BusinessException(ErrorCode.NTS_TAX_VALIDATED);
        }

        OcrTaxInvoiceResponseDTO result = homeTaxService.verifyTaxInvoice(
            OcrTaxInvoiceRequestDTO.from(ntsTax));

        if ("1".equals(result.getResAuthenticity())) {
            ntsTax.updateStatus(Status.APPROVAL);
        } else {
            ntsTax.updateStatus(Status.REJECTION);
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetCsvResponseDTO> getHometaxCsv(Member member, LocalDate startDate,
        LocalDate endDate, List<String> suNameList, List<String> ipNameList, Status status) {
        if (status == Status.WAITING) {
            throw new BusinessException(ErrorCode.WAITING_NOT_SELECTED);
        }
        List<NtsTax> ntsTaxList;

        if (status == null) {
            List<Status> validStatuses = Arrays.asList(Status.APPROVAL, Status.REJECTION);
            ntsTaxList = ntsTaxRepository.findByMemberAndStatusIn(member, validStatuses);
        } else {
            ntsTaxList = ntsTaxRepository.findByMemberAndStatus(member, status);
        }

        return ntsTaxRepository.getHometaxCsv(member, startDate, endDate, suNameList, ipNameList,
            ntsTaxList);
    }
}
