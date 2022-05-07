package com.shop.repository;

import com.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Memeber 엔티티 DB저장
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email); //회원가입시 중복된 회원이 있는지 이메일을 검사
}
