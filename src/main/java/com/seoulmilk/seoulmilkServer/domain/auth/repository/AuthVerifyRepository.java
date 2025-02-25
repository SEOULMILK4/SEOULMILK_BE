package com.seoulmilk.seoulmilkServer.domain.auth.repository;

import com.seoulmilk.seoulmilkServer.domain.auth.domain.AuthVerifiedMember;
import org.springframework.data.repository.CrudRepository;

public interface AuthVerifyRepository extends CrudRepository<AuthVerifiedMember,String> {

}
