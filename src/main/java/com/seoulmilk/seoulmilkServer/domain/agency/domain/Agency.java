package com.seoulmilk.seoulmilkServer.domain.agency.domain;

import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Agency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String agencyName;

    @Column(name = "agency_id", unique = true)
    private String agencyId;

    private String password;

    @NotNull
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private ApprovedState approvedState;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = true)
    private Member member;

    public void updateAgencyInfo(String agencyId, String password) {
        this.agencyId = agencyId;
        this.password = password;
    }

    public void updateAgencyEmail(String email) {
        this.email = email;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void registerMember(Member member) { // 대리점 담당 사원 배정
        this.member = member;
    }

    public void updateAgencyStatue() {
        this.approvedState = ApprovedState.APPROVED;
    }


    @Builder
    private Agency(Long id, String agencyName, String agencyId, String password, String email,
        ApprovedState approvedState, Member member) {
        this.id = id;
        this.agencyName = agencyName;
        this.agencyId = agencyId;
        this.password = password;
        this.email = email;
        this.approvedState = approvedState;
        this.member = member;
    }

    public static Agency of(String agencyName, String email) {
        return Agency.builder()
            .agencyName(agencyName)
            .email(email)
            .approvedState(ApprovedState.DISAPPROVED)
            .build();
    }


}
