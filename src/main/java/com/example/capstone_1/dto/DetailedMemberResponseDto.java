package com.example.capstone_1.dto;

import com.example.capstone_1.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DetailedMemberResponseDto {
    private String email;
    private String phoneNumber;
    private String name;
    private String bankName;
    private String account;

    public static DetailedMemberResponseDto of(Member member) {
        return new DetailedMemberResponseDto(
                member.getEmail(),
                member.getPhoneNumber(),
                member.getName(),
                member.getBankName(),
                member.getAccount()
        );
    }
}
