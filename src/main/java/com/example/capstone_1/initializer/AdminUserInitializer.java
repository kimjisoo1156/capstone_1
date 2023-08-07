package org.zerock.b01.initializer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.zerock.b01.domain.Authority;
import org.zerock.b01.domain.Member;
import org.zerock.b01.repository.MemberRepository;

@Component
@RequiredArgsConstructor   //프로젝트 시작시 관리자 회원 db에 값 박아 놓기.
public class AdminUserInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // 데이터베이스에 관리자 회원이 이미 존재하는지 체크
        if (!memberRepository.existsByEmail("admin@example.com")) {
            // 관리자 회원 추가
            Member admin = Member.builder()
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("admin123"))
                    .name("admin")
                    .phoneNumber("010-1234-5678")
                    .bankName("KB국민은행")
                    .account("0222-1222-122112")
                    .authority(Authority.ROLE_ADMIN)
                    .build();
            memberRepository.save(admin);
        }
    }
}
