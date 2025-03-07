package com.seoulmilk.seoulmilkServer.domain.ntsTax.repository;

import com.seoulmilk.seoulmilkServer.domain.ntsTax.domain.NtsTax;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NtsTaxRepository extends JpaRepository<NtsTax, Long>, NtsTaxRepositoryCustom {


}
