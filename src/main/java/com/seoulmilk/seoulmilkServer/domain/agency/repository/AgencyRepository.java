package com.seoulmilk.seoulmilkServer.domain.agency.repository;

import com.seoulmilk.seoulmilkServer.domain.agency.domain.Agency;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgencyRepository extends JpaRepository<Agency,Long> {

    Optional<Agency> findByEmail(String email);

    Optional<Agency> findByAgencyId(String agencyId);

    Optional<Agency> findByAgencyIdAndEmail(String agencyId, String email);

    boolean existsByAgencyName(String agencyName);

    boolean existsByEmail(String email);

}
