package com.seoulmilk.seoulmilkServer.domain.auth.repository;

import com.seoulmilk.seoulmilkServer.domain.auth.domain.RefreshTokenEntity;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshTokenEntity, String> {

    void deleteByUserIdAndRole(String userId, String role);

}
