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

    @Query("SELECT m.id AS id, m.name AS name, m.email AS email, COUNT(a) AS agencyCount " +
        "FROM Member m LEFT JOIN Agency a ON m.id = a.member.id " +
        "GROUP BY m.id")
    List<GetEmployeeWithAgencyResponseDTO> findAllMembersWithAgencyCount();

}
