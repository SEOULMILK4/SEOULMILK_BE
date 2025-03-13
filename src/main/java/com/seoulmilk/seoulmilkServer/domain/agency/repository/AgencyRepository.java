package com.seoulmilk.seoulmilkServer.domain.agency.repository;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgencyRepository extends JpaRepository<Agency,Long> {

    Optional<Agency> findByEmail(String email);

    Optional<Agency> findByAgencyId(String agencyId);

    Optional<Agency> findByAgencyIdAndEmail(String agencyId, String email);

    boolean existsByAgencyName(String agencyName);

    boolean existsByEmail(String email);

    List<Agency> findByMember(Member member);

    List<Agency> findByMemberIsNull();

    List<Agency> findAllByIdInAndMemberIsNull(List<Long> ids);

    Page<Agency> findAllByAgencyIdIsNotNull(Pageable pageable);


}
