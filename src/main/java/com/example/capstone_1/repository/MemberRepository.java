package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.b01.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email); //이메일 찾기 = 아이디 찾기
    boolean existsByEmail(String email); //이메일 중복 == 아이디 중복
}
