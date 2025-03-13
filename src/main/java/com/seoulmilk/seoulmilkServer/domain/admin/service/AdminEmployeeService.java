package com.seoulmilk.seoulmilkServer.domain.admin.service;

import com.seoulmilk.seoulmilkServer.domain.admin.domain.Admin;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.agency.GetEmployeeWithAgencyResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.employee.GetOneAgencyByEmployeeResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.employee.GetOneEmployeeResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.employee.PostAssignAgeciesRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.employee.PostEmployeeRequestDTO;
import com.seoulmilk.seoulmilkServer.domain.admin.dto.employee.PostEmployeeResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.agency.repository.AgencyRepository;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.member.repository.MemberRepository;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import com.seoulmilk.seoulmilkServer.global.etc.MemberProperties;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminEmployeeService {

    private final MemberRepository memberRepository;
    private final AgencyRepository agencyRepository;
    private final AdminAuthService adminAuthService;
    private final MemberProperties memberProperties;
    private final PasswordEncoder encoder;

    @Transactional(readOnly = true)
    public Page<GetEmployeeWithAgencyResponseDTO> getEmployeeList(Integer page) {

        Admin admin = adminAuthService.getCurrentAdmin();

        Pageable pageable = PageRequest.of(page, 13);

        return memberRepository.findAllMembersWithAgencyCount(pageable);
    }

    @Transactional(readOnly = true)
    public GetOneEmployeeResponseDTO getOneEmployee(Long employeeId) {

        Admin admin = adminAuthService.getCurrentAdmin();

        Member member = memberRepository.findById(employeeId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        List<GetOneAgencyByEmployeeResponseDTO> agencies = member.getAgencies().stream()
            .map(GetOneAgencyByEmployeeResponseDTO::from)
            .toList();

        return GetOneEmployeeResponseDTO.from(member, agencies);
    }

    // 사원 등록
    @Transactional
    public PostEmployeeResponseDTO postEmployeeRegister(PostEmployeeRequestDTO requestDTO) {

        Admin admin = adminAuthService.getCurrentAdmin();
        String employeeNum = requestDTO.getEmployeeNum();

        if (memberRepository.existsByEmployeeNum(employeeNum)) {
            throw new BusinessException(ErrorCode.MEMBER_ALREADY_REGISTERED);
        }

        String encodePassword = encoder.encode(memberProperties.getDefaultPassword());
        Member employee = Member.of(employeeNum, requestDTO.getName(), requestDTO.getEmail(),
            encodePassword);
        memberRepository.save(employee);

        return PostEmployeeResponseDTO.of(employee);

    }

    @Transactional
    public List<PostEmployeeResponseDTO> postEmployeesRegister(
        List<PostEmployeeRequestDTO> employees) {

        Admin admin = adminAuthService.getCurrentAdmin();
        System.out.println("초기비번" + memberProperties.getDefaultPassword());
        String encodePassword = encoder.encode(memberProperties.getDefaultPassword());

        List<Member> newEmployees = employees.stream()
            .filter(employee -> !memberRepository.existsByEmployeeNum(employee.getEmployeeNum()))
            .map(employee -> Member.of(employee.getEmployeeNum(), employee.getName(),
                employee.getEmail(), encodePassword))
            .toList();

        List<Member> savedEmployees = memberRepository.saveAll(newEmployees);

        return savedEmployees.stream()
            .map(PostEmployeeResponseDTO::of)
            .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<GetOneAgencyByEmployeeResponseDTO> getPossibleAgencyList(Long employeeId) {

        Admin admin = adminAuthService.getCurrentAdmin();

        Member member = memberRepository.findById(employeeId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        return agencyRepository.findByMemberIsNull().stream()
            .map(GetOneAgencyByEmployeeResponseDTO::from)
            .collect(Collectors.toList());

    }

    @Transactional
    public List<GetOneAgencyByEmployeeResponseDTO> assignAgencies(Long employeeId,
        PostAssignAgeciesRequestDTO requestDTO) {

        Admin admin = adminAuthService.getCurrentAdmin();

        Member member = memberRepository.findById(employeeId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        List<Agency> agencies = agencyRepository.findAllByIdInAndMemberIsNull(requestDTO.getIdList());

        return agencies.stream()
            .map(agency -> {
                agency.assignMember(member);
                return GetOneAgencyByEmployeeResponseDTO.from(agency);
            })
            .collect(Collectors.toList());
    }
}

