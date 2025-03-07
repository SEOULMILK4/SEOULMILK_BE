package com.seoulmilk.seoulmilkServer.domain.member.domain;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.global.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(mappedBy = "member")
    private List<Agency> agencies = new ArrayList<>();


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

    public static Member of(String employeeNum, String name, String email, String encodedPassword) {
        return Member.builder()
            .employeeNum(employeeNum)
            .name(name)
            .email(email)
            .password(encodedPassword)
            .build();
    }

}
