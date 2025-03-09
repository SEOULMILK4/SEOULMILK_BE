package com.seoulmilk.seoulmilkServer.domain.ntsTax.repository;

import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.IsSuccess;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface NtsTaxRepository extends JpaRepository<NtsTax, Long>, NtsTaxRepositoryCustom {
    // 세금 계산서 목록 조회
    Page<NtsTax> findByAgencyIdAndIsSuccess(Long agencyId, IsSuccess isSuccess, Pageable pageable);
    Long countByIsSuccess(IsSuccess isSuccess);

    // 세금 계산서 진위 여부 검증 후, 검색
    @Query("SELECT n FROM NtsTax n " +
            "WHERE n.member = :member " +
            "AND n.createdAt BETWEEN :startDateTime AND :endDateTime " +
            "AND (COALESCE(:status, n.status) = n.status)")
    Page<NtsTax> findAllByFilters(@Param("member") Member member,
                                  @Param("startDateTime") LocalDateTime startDateTime,
                                  @Param("endDateTime") LocalDateTime endDateTime,
                                  @Param("status") Status status,
                                  Pageable pageable
                                  );
}
