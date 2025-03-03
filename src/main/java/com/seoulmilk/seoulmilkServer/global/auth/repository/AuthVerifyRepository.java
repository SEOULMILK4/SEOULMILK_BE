package com.seoulmilk.seoulmilkServer.global.auth.repository;

import com.seoulmilk.seoulmilkServer.global.auth.domain.AuthVerifiedMember;
import org.springframework.data.repository.CrudRepository;

public interface AuthVerifyRepository extends CrudRepository<AuthVerifiedMember,String> {

}
