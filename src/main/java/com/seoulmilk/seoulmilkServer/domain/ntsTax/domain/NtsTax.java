package com.seoulmilk.seoulmilkServer.domain.ntsTax.domain;

import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NtsTax extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "mySequence")
    @SequenceGenerator(name = "mySequence", sequenceName = "nts_tax_seq", allocationSize = 1)
    @Column(name = "nts_tax_id")
    private Long id;

    @Column(name = "issue_id", nullable = false)
    private String issueId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ARAP ARAP;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "su_id", nullable = false)
    private String suId;

    @Column(name = "su_name", nullable = false)
    private String suName;

    @Column(name = "ip_id", nullable = false)
    private String ipId;

    @Column(name = "ip_name", nullable = false)
    private String ipName;

    @Column(name = "charge_total", nullable = false)
    private String chargeTotal;

    @Column(name = "tax_total", nullable = false)
    private String taxTotal;

    @Column(name = "grand_total", nullable = false)
    private String grandTotal;

    @Column(name = "imageUrl")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "agency_id")
//    private Agency agency;

    public void updateNtsTax(String issueId, LocalDate issueDate, String suId, String ipId, String chargeTotal, String taxTotal, String grandTotal) {
        this.issueId = issueId;
        this.issueDate = issueDate;
        this.suId = suId;
        this.ipId = ipId;
        this.chargeTotal = chargeTotal;
        this.taxTotal = taxTotal;
        this.grandTotal = grandTotal;
    }

    @Builder
    private NtsTax (Long id, String issueId, ARAP ARAP, LocalDate issueDate, String suId, String suName, String ipId, String ipName, String chargeTotal, String taxTotal, String grandTotal, String imageUrl) {
        this.id = id;
        this.issueId = issueId;
        this.ARAP = ARAP;
        this.issueDate = issueDate;
        this.suId = suId;
        this.suName = suName;
        this.ipId = ipId;
        this.ipName = ipName;
        this.chargeTotal = chargeTotal;
        this.taxTotal = taxTotal;
        this.grandTotal = grandTotal;
        this.imageUrl = imageUrl;
    }
}