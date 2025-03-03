package com.seoulmilk.seoulmilkServer.domain.admin.repository;

import com.seoulmilk.seoulmilkServer.domain.admin.domain.Admin;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Long> {

    Optional<Admin> findByMasterKey(String masterKey);

}
