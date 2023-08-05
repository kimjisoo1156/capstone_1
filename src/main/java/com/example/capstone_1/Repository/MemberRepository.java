package com.example.capstone_1.repository;


import com.example.capstone_1.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email); //이메일 찾기 = 아이디 찾기
    boolean existsByEmail(String email); //이메일 중복 == 아이디 중복
}
