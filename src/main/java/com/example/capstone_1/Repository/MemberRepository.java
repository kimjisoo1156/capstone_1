package com.example.capstone_1.Repository;

import com.example.capstone_1.Entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String> {

    public MemberEntity findByMemberEmail(String memberEmail);

}
