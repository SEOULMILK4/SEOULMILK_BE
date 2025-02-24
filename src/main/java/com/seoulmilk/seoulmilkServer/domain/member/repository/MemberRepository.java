package com.seoulmilk.seoulmilkServer.domain.member.repository;

import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmployeeNum(String employeeNum);

}
