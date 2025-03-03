package com.seoulmilk.seoulmilkServer.global.auth.repository;

import com.seoulmilk.seoulmilkServer.global.auth.domain.RefreshTokenEntity;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshTokenEntity, String> {


}
