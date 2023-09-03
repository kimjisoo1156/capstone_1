package com.example.capstone_1.service;

import com.example.capstone_1.domain.Member;
import com.example.capstone_1.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private MemberRepository memberRepository;
    @Override
    public String getLoggedInUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    @Transactional
    public void updatePassword(String email, String newPassword) {
        // 이메일을 기반으로 회원을 찾아 새로운 비밀번호로 업데이트합니다.
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일을 찾을 수 없습니다."));
        member.setPassword(newPassword);
        memberRepository.save(member);
    }
}
