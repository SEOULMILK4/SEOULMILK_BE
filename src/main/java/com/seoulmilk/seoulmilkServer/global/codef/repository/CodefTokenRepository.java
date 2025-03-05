package com.seoulmilk.seoulmilkServer.global.codef.repository;

import com.seoulmilk.seoulmilkServer.global.codef.domain.CodefAccessToken;
import org.springframework.data.repository.CrudRepository;

public interface CodefTokenRepository extends CrudRepository<CodefAccessToken,Long> {

}
