package com.seoulmilk.seoulmilkServer.domain.member.repository;

import com.seoulmilk.seoulmilkServer.domain.admin.dto.GetEmployeeWithAgencyResponseDTO;
import com.seoulmilk.seoulmilkServer.domain.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmployeeNum(String employeeNum);

    Optional<Member> findByEmployeeNumAndEmail(String employeeNum, String email);

    @Query("SELECT new com.seoulmilk.seoulmilkServer.domain.admin.dto.GetEmployeeWithAgencyResponseDTO(m.id, m.name,m.employeeNum, m.email, COUNT(a.id)) " +
           "FROM Member m LEFT JOIN m.agencies a " +
           "GROUP BY m.id, m.employeeNum, m.name, m.email")
    List<GetEmployeeWithAgencyResponseDTO> findAllMembersWithAgencyCount();

}
