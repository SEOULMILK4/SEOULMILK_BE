package com.seoulmilk.seoulmilkServer.domain.ntsTax.repository;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.IsSuccess;
import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NtsTaxRepository extends JpaRepository<NtsTax, Long>, NtsTaxRepositoryCustom {
    // 본사, 대리점 - 세금 계산서 전체 삭제
    List<NtsTax> findByAgencyIdAndStatus(Long agencyId, Status status);
    List<NtsTax> findByMemberIdAndStatusIn(Long memberId, List<Status> statuses);

    // 대리점 - 세금 계산서 목록 조회
    Page<NtsTax> findByAgencyIdAndIsSuccess(Long agencyId, IsSuccess isSuccess, Pageable pageable);
    Long countByIsSuccess(IsSuccess isSuccess);

    // 본사 - 세금 계산서 이번 달 내역 조회
    @Query("SELECT n FROM NtsTax n WHERE n.member = :member AND n.status = :status " +
            "AND EXTRACT(YEAR FROM n.createdAt) = EXTRACT(YEAR FROM CURRENT_DATE) " +
            "AND EXTRACT(MONTH FROM n.createdAt) = EXTRACT(MONTH FROM CURRENT_DATE)")
    Page<NtsTax> findByMemberAndStatusThisMonth(@Param("member") Member member,
                                                @Param("status") Status status,
                                                Pageable pageable);

    @Query("SELECT COUNT(n) FROM NtsTax n WHERE n.status = :status " +
           "AND EXTRACT(YEAR FROM n.createdAt) = EXTRACT(YEAR FROM CURRENT_DATE) " +
           "AND EXTRACT(MONTH FROM n.createdAt) = EXTRACT(MONTH FROM CURRENT_DATE)")
    Long countByStatusThisMonth(@Param("status") Status status);

    // 본사 - 세금 계산서 전체 내역 통합 조회
    Page<NtsTax> findByMemberAndStatusIn(Member member, List<Status> statusList, Pageable pageable);

    @Query("SELECT n FROM NtsTax n WHERE n.member = :member AND n.status = :status ")
    Page<NtsTax> findByMemberAndStatus(@Param("member") Member member,
                                       @Param("status") Status status,
                                       Pageable pageable);
    Long countByStatus(Status status);

    List<NtsTax> findAllByAgencyIdAndStatus(Long agencyId,Status status);

    // 세금 계산서 csv 추출
    List<NtsTax> findByMemberAndStatusIn(Member member, List<Status> statuseList);

    @Query("SELECT n FROM NtsTax n WHERE n.member = :member AND n.status = :status ")
    List<NtsTax> findByMemberAndStatus(@Param("member") Member member,
                                       @Param("status") Status status);

}
