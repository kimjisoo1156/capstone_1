package com.example.capstone_1.service;

import com.example.capstone_1.dto.DetailedMemberResponseDto;
import com.example.capstone_1.dto.MemberRequestDto;

public interface MemberService {
    String getLoggedInUserEmail();
    void updatePassword(String email, String newPassword);

    DetailedMemberResponseDto getMemberByEmail(String email);

    void updateMember(String email, MemberRequestDto memberRequestDto);
    void deleteMemberByEmail(String email);

}
