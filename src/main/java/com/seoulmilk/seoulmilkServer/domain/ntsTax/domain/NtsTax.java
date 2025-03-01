package com.seoulmilk.seoulmilkServer.domain.ntsTax.domain;

import com.seoulmilk.seoulmilkServer.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

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

    @Column(name = "ip_id", nullable = false)
    private String ipId;

    @Column(name = "charge_total", nullable = false)
    private String chargeTotal;

    @Column(name = "tax_total", nullable = false)
    private String taxTotal;

    @Column(name = "grand_total", nullable = false)
    private String grandTotal;

    @Column(name = "created_time", nullable = false)
    private LocalTime createdTime;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Builder
    private NtsTax (Long id, String issueId, ARAP ARAP, LocalDate issueDate, String suId, String ipId, String chargeTotal, String taxTotal, String grandTotal, LocalTime createdTime, String imageUrl) {
        this.id = id;
        this.issueId = issueId;
        this.ARAP = ARAP;
        this.issueDate = issueDate;
        this.suId = suId;
        this.ipId = ipId;
        this.chargeTotal = chargeTotal;
        this.taxTotal = taxTotal;
        this.grandTotal = grandTotal;
        this.createdTime = createdTime;
        this.imageUrl = imageUrl;
    }
}