package com.example.capstone_1.initializer;

import com.example.capstone_1.domain.Authority;
import com.example.capstone_1.domain.Member;
import com.example.capstone_1.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor   //프로젝트 시작시 관리자 회원 db에 값 박아 놓기.
public class AdminUserInitializer implements ApplicationListener<ApplicationReadyEvent> {
    @Value("${admin_id}")
    private String id;

    @Value("${admin_password}")
    private String password;

    @Value("${admin_name}")
    private String name;

    @Value("${admin_phone_number}")
    private String phone_number;


    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // 데이터베이스에 관리자 회원이 이미 존재하는지 체크
        if (!memberRepository.existsByEmail(id)) {
            // 관리자 회원 추가
            Member admin = Member.builder()
                    .email(id)
                    .password(passwordEncoder.encode(password))
                    .name(name)
                    .phoneNumber(phone_number)
                    .authority(Authority.ROLE_ADMIN)
                    .build();
            memberRepository.save(admin);
        }
    }
}
