package org.zerock.b01.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.b01.domain.Authority;
import org.zerock.b01.domain.Member;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto { //회원가입할때 필요한 필드들,

    private String email;
    private String password;
    private String phoneNumber;
    private String name;
    private String bankName;
    private String account;


    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .phoneNumber(phoneNumber)
                .name(name)
                .bankName(bankName)
                .account(account)
                .authority(Authority.ROLE_USER)
                .build();
    }

}
