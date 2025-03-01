package com.seoulmilk.seoulmilkServer.global.security;

import com.seoulmilk.seoulmilkServer.domain.admin.domain.Admin;
import com.seoulmilk.seoulmilkServer.domain.admin.repository.AdminRepository;
import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.agency.repository.AgencyRepository;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.member.repository.MemberRepository;
import com.seoulmilk.seoulmilkServer.global.error.ErrorCode;
import com.seoulmilk.seoulmilkServer.global.error.exception.BusinessException;
import com.seoulmilk.seoulmilkServer.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService //implements UserDetailsService
{

    private final MemberRepository memberRepository;
    private final AgencyRepository agencyRepository;
    private final AdminRepository adminRepository;

    public CustomUserDetails loadUserByIdAndRole(String userId, String role) {
        Object user;
        String userType;

        switch (role) {
            case "employee":
                user = memberRepository.findById(Long.parseLong(userId))
                    .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
                userType = "employee";
                break;
            case "agency":
                user = agencyRepository.findById(Long.parseLong(userId))
                    .orElseThrow(() -> new BusinessException(ErrorCode.AGENCY_NOT_FOUND));
                userType = "agency";
                break;
            case "admin":
                user = adminRepository.findById(Long.parseLong(userId))
                    .orElseThrow(() -> new BusinessException(ErrorCode.ADMIN_NOT_FOUND));
                userType = "admin";
                break;
            default:
                throw new BusinessException(ErrorCode.ROLE_INVALID);
        }
        return new CustomUserDetails(user, userType);
    }

    //    @Override
    //    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
    //        Member member = memberRepository.findByEmployeeNum(employeeNum)
    //                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    //
    //        return new CustomUserDetails(member);
    //    }

}
