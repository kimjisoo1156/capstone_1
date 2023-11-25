package com.example.capstone_1.dto;

import com.example.capstone_1.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
    private String email;

    private String errorMessage;  // 오류 메시지를 저장하는 새로운 필드
    public static MemberResponseDto of(Member member) {
        return new MemberResponseDto(member.getEmail(),null);
    }

    public static MemberResponseDto error(String errorMessage) {
        MemberResponseDto responseDto = new MemberResponseDto();
        responseDto.setErrorMessage(errorMessage);
        return responseDto;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
//    public static MemberResponseDto of(Member member) {
//        return new MemberResponseDto(member.getEmail());
//    }
//
}
