package com.example.capstone_1.controller;

import com.example.capstone_1.dto.MemberResponseDto;
import com.example.capstone_1.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController { //비밀번호 찾기

    private final MemberService memberService;

//    @GetMapping("/me")
//    public ResponseEntity<MemberResponseDto> findMemberInfoById() {
//        return ResponseEntity.ok(memberService.findMemberInfoById(SecurityUtil.getCurrentMemberId()));
//    }

//    @GetMapping("/{email}")
//    public ResponseEntity<MemberResponseDto> findMemberInfoByEmail(@PathVariable String email) {
//        return ResponseEntity.ok(memberService.findMemberInfoByEmail(email));
//    }
}
