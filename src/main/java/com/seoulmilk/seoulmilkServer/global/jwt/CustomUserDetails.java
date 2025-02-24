package com.seoulmilk.seoulmilkServer.global.jwt;

import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import java.util.Collection;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Member member; // Member 엔티티를 담을 필드

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(member.getRole().name()));
    }

    @Override
    public String getPassword() {
        return member.getPassword(); // Member 엔티티에서 비밀번호 가져오기
    }

    @Override
    public String getUsername() {
        return member.getEmail(); // Member 엔티티에서 이메일(ID 역할)을 가져오기
    }

    public String getEmployeeNum(){return member.getEmployeeNum();}

    public Long getMemberId(){return member.getId();}

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

    public Member getMember() {
        return member;
    }
}
