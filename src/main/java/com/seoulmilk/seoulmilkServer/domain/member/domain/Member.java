package com.seoulmilk.seoulmilkServer.domain.member.domain;

import com.seoulmilk.seoulmilkServer.global.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String employeeNum;

    @NotNull
    private String password;

    @NotNull
    private String email;

    @NotNull
    private String name;


    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    @Builder
    private Member(Long id, String employeeNum, String password, String email, String name
    ) {
        this.id = id;
        this.employeeNum = employeeNum;
        this.password = password;
        this.email = email;
        this.name = name;
    }

}
