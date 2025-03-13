package com.seoulmilk.seoulmilkServer.domain.admin.service;

import com.seoulmilk.seoulmilkServer.domain.admin.domain.Admin;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.agency.GetAgencyResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.agency.InviteAgenciesRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.agency.PostAdminRegisterAgencyRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.agency.PostAdminRegisterAgencyResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.agency.UpdateAgencyRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.agency.UpdateAgencyResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.agency.domain.ApprovedState;
import com.seoulmilk.seoulmilkServer.domain.agency.repository.AgencyRepository;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import com.seoulmilk.seoulmilkServer.global.mail.service.EmailService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class AdminAgencyService {

    private final AgencyRepository agencyRepository;
    private final AdminAuthService adminAuthService;
    private final EmailService emailService;

    @Transactional(readOnly = true)
    public Page<Agency> getAgencyList(Integer page) {
        Pageable pageable = PageRequest.of(0, 13);

        Admin admin = adminAuthService.getCurrentAdmin();

        return agencyRepository.findAll(pageable);
    }


    @Transactional(readOnly = true)
    public Page<Agency> getJoinedAgencyList(Integer page) {
        Pageable pageable = PageRequest.of(0, 13);

        Admin admin = adminAuthService.getCurrentAdmin();

        return agencyRepository.findAllByAgencyIdIsNotNull(pageable);
    }

    @Transactional(readOnly = true)
    public GetAgencyResponseDTO getOneAgency(Long agencyId) {

        Admin admin = adminAuthService.getCurrentAdmin();

        Agency agency = agencyRepository.findById(agencyId)
            .orElseThrow(() -> new BusinessException(ErrorCode.AGENCY_NOT_FOUND));

        return GetAgencyResponseDTO.from(agency);
    }


    @Transactional
    public UpdateAgencyResponseDTO updateAgencyInfo(Long agencyId,
        UpdateAgencyRequestDTO requestDTO) {

        Admin admin = adminAuthService.getCurrentAdmin();

        Agency agency = agencyRepository.findById(agencyId)
            .orElseThrow(() -> new BusinessException(ErrorCode.AGENCY_NOT_FOUND));

        agency.updateAgencyEmail(requestDTO.getEmail());

        return UpdateAgencyResponseDTO.of(agency, requestDTO.getEmail());
    }

    @Transactional
    public PostAdminRegisterAgencyResponseDTO postAgencyRegister(
        PostAdminRegisterAgencyRequestDTO requestDTO) {

        Admin admin = adminAuthService.getCurrentAdmin();

        String agencyName = requestDTO.getAgencyName();
        String agencyEmail = requestDTO.getEmail();

        if (agencyRepository.existsByAgencyName(agencyName) || agencyRepository.existsByEmail(
            agencyEmail)) {
            throw new BusinessException(ErrorCode.AGENCY_ALREADY_REGISTERED);
        }

        Agency newAgency = Agency.of(agencyName, agencyEmail);
        agencyRepository.save(newAgency);

        return PostAdminRegisterAgencyResponseDTO.of(newAgency);
    }

    @Transactional
    public List<PostAdminRegisterAgencyResponseDTO> postAgenciesRegister(
        List<PostAdminRegisterAgencyRequestDTO> agencies) {

        Admin admin = adminAuthService.getCurrentAdmin();

        List<Agency> newAgencies = agencies.stream()
            .filter(agency -> !agencyRepository.existsByAgencyName(agency.getAgencyName()) &&
                !agencyRepository.existsByEmail(agency.getEmail()))
            .map(request -> Agency.of(request.getAgencyName(), request.getEmail()))
            .collect(Collectors.toList());

        List<Agency> savedAgencies = agencyRepository.saveAll(newAgencies);

        return savedAgencies.stream()
            .map(PostAdminRegisterAgencyResponseDTO::of)
            .collect(Collectors.toList());

    }

    // 초대 메일 보내기
    @Transactional
    public void inviteAgencies(InviteAgenciesRequestDTO requestDTO) {

        Admin admin = adminAuthService.getCurrentAdmin();

        for (Long id : requestDTO.getIdList()) {
            Agency agency = agencyRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.AGENCY_DISAPPROVED));
            if (agency.getApprovedState() == ApprovedState.APPROVED) {
                throw new BusinessException(ErrorCode.AGENCY_ALREADY_REGISTERED);
            }
            emailService.sendAgencyInvitation(agency.getEmail(), agency.getAgencyName());
            agency.updateAgencyStatue();
        }
    }


}
