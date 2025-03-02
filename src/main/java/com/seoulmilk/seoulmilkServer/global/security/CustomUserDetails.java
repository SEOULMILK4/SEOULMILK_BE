package com.seoulmilk.seoulmilkServer.global.security;

import com.seoulmilk.seoulmilkServer.domain.admin.domain.Admin;
import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import java.util.Collection;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Object user;
    private final String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
    }

    @Override
    public String getPassword() {
        if (user instanceof Member) {
            return ((Member) user).getPassword();
        }
        if (user instanceof Agency) {
            return ((Agency) user).getPassword();
        }
        if (user instanceof Admin) {
            return ((Admin) user).getMasterKey();
        }
        return null;
    }

    @Override
    public String getUsername() {
        if (user instanceof Member) {
            return ((Member) user).getName();
        }
        return null;
    }

    public String getEmployeeNum() {
        if (user instanceof Member) {
            return ((Member) user).getEmployeeNum();
        }
        return null;
    }

    public Long getUserId() {
        if (user instanceof Member) {
            return ((Member) user).getId();
        }
        if (user instanceof Agency) {
            return ((Agency) user).getId();
        }
        if (user instanceof Admin) {
            return ((Admin) user).getId();
        }
        return null;
    }

    public String getUserRole() {
        if (user instanceof Member) {
            return "employee";
        }
        if (user instanceof Agency) {
            return "agency";
        }
        if (user instanceof Admin) {
            return "admin";
        }
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

//    public Member getMember() {
//        return member;
//    }
}
