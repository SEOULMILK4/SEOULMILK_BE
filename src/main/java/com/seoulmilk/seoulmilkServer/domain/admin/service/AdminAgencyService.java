package com.seoulmilk.seoulmilkServer.domain.admin.service;

import com.seoulmilk.seoulmilkServer.domain.admin.domain.Admin;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.agency.GetAgencyResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.agency.InviteAgenciesRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.agency.PostAgencyRegisterRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.agency.PostAgencyRegisterResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.agency.UpdateAgencyRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.agency.UpdateAgencyResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.agency.repository.AgencyRepository;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import com.seoulmilk.seoulmilkServer.global.mail.service.EmailService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class AdminAgencyService {

    private final AgencyRepository agencyRepository;
    private final AdminAuthService adminAuthService;
    private final EmailService emailService;

    @Transactional(readOnly = true)
    public List<GetAgencyResponseDTO> getAgencyList() {

        Admin admin = adminAuthService.getCurrentAdmin();

        List<Agency> agency = agencyRepository.findAll();

        return agency.stream()
            .map(GetAgencyResponseDTO::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public GetAgencyResponseDTO getOneAgency(Long agencyId) {

        Admin admin = adminAuthService.getCurrentAdmin();

        Agency agency = agencyRepository.findById(agencyId)
            .orElseThrow(() -> new BusinessException(ErrorCode.AGENCY_NOT_FOUND));

        return GetAgencyResponseDTO.from(agency);
    }


    @Transactional
    public UpdateAgencyResponseDTO updateAgencyInfo(UpdateAgencyRequestDTO requestDTO) {

        Admin admin = adminAuthService.getCurrentAdmin();

        Agency agency = agencyRepository.findById(requestDTO.getId())
            .orElseThrow(() -> new BusinessException(ErrorCode.AGENCY_NOT_FOUND));

        agency.updateAgencyEmail(requestDTO.getEmail());

        return UpdateAgencyResponseDTO.of(agency, requestDTO.getEmail());
    }

    @Transactional
    public PostAgencyRegisterResponseDTO postAgencyRegister(
        PostAgencyRegisterRequestDTO requestDTO) {

        Admin admin = adminAuthService.getCurrentAdmin();

        String agencyName = requestDTO.getAgencyName();
        String agencyEmail = requestDTO.getEmail();

        if (agencyRepository.existsByAgencyName(agencyName) || agencyRepository.existsByEmail(
            agencyEmail)) {
            throw new BusinessException(ErrorCode.AGENCY_ALREADY_REGISTERED);
        }

        Agency newAgency = Agency.of(agencyName, agencyEmail);
        agencyRepository.save(newAgency);

        return PostAgencyRegisterResponseDTO.of(newAgency);
    }

    @Transactional
    public List<PostAgencyRegisterResponseDTO> postAgenciesRegister(
        List<PostAgencyRegisterRequestDTO> agencies) {

        Admin admin = adminAuthService.getCurrentAdmin();

        List<Agency> newAgencies = agencies.stream()
            .filter(agency -> !agencyRepository.existsByAgencyName(agency.getAgencyName()) &&
                !agencyRepository.existsByEmail(agency.getEmail()))
            .map(request -> Agency.of(request.getAgencyName(), request.getEmail()))
            .collect(Collectors.toList());

        List<Agency> savedAgencies = agencyRepository.saveAll(newAgencies);

        return savedAgencies.stream()
            .map(PostAgencyRegisterResponseDTO::of)
            .collect(Collectors.toList());

    }

    // 초대 메일 보내기
    @Transactional
    public void inviteAgencies(InviteAgenciesRequestDTO requestDTO) {

        Admin admin = adminAuthService.getCurrentAdmin();

        for (Long id : requestDTO.getIdList()) {
            Agency agency = agencyRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.AGENCY_DISAPPROVED));
            emailService.sendOtp(agency.getEmail(), agency.getAgencyName());
            agency.updateAgencyStatue();
        }
    }


}
