package com.springboot.ezenbackend.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.ezenbackend.domain.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
    @EntityGraph(attributePaths = { "memberRoleList" })
    @Query("SELECT m FROM Member m WHERE m.email = :email") // JPQL JPA 전용 AQL. 검색 대상이 테이블이 아닌 Entity 객체이다,
    Member getWithRoles(@Param("email") String email);
}
