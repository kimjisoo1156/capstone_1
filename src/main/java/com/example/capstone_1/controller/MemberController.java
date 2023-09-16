package com.example.capstone_1.controller;

import com.example.capstone_1.dto.DetailedMemberResponseDto;
import com.example.capstone_1.dto.MemberRequestDto;
import com.example.capstone_1.dto.MemberResponseDto;
import com.example.capstone_1.dto.ResetPasswordRequest;
import com.example.capstone_1.service.MemberService;
import com.example.capstone_1.service.UserService;
import com.example.capstone_1.utill.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController { //임시 비밀번호 수정
    private final MemberService memberService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

//    @GetMapping("/me")
//    public ResponseEntity<MemberResponseDto> findMemberInfoById() {
//        return ResponseEntity.ok(memberService.findMemberInfoById(SecurityUtil.getCurrentMemberId()));
//    }

//    @GetMapping("/{email}")
//    public ResponseEntity<MemberResponseDto> findMemberInfoByEmail(@PathVariable String email) {
//        return ResponseEntity.ok(memberService.findMemberInfoByEmail(email));
//    }

    @GetMapping("/me") //현재 로그인한 회원의 정보 조회
    public ResponseEntity<DetailedMemberResponseDto> getCurrentUser() { //조회할때 password 없음
        String loggedInUserEmail = userService.getLoggedInUserEmail();
        if (loggedInUserEmail != null) {
            DetailedMemberResponseDto memberResponseDto = userService.getMemberByEmail(loggedInUserEmail);
            return ResponseEntity.ok(memberResponseDto);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping(value = "/me", produces="application/json; charset=utf8") //현재 로그인된 회원의 정보 수정 password수정가능 & 이메일은 수정 불가능
    public ResponseEntity<String> updateCurrentUser(@RequestBody MemberRequestDto memberRequestDto) {
        String loggedInUserEmail = userService.getLoggedInUserEmail();
        if (loggedInUserEmail != null) {
            userService.updateMember(loggedInUserEmail, memberRequestDto);
            return ResponseEntity.ok("회원 정보가 업데이트되었습니다.");
        }
        return ResponseEntity.notFound().build();
    }
    @DeleteMapping(value = "/me", produces="application/json; charset=utf8") // 현재 로그인한 회원의 정보를 삭제 (회원 탈퇴)
    public ResponseEntity<String> deleteCurrentUser() {
        String loggedInUserEmail = userService.getLoggedInUserEmail();
        if (loggedInUserEmail != null) {
            userService.deleteMemberByEmail(loggedInUserEmail);
            return ResponseEntity.ok("회원 탈퇴가 성공적으로 처리되었습니다.");
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping(value = "/reset-password", produces="application/json; charset=utf8") //그럼 이제.. 이게 필요가 없징.
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        // 1. 현재 사용자의 이메일 주소 가져오기
        String userEmail = userService.getLoggedInUserEmail();
        if (userEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인한 사용자를 찾을 수 없습니다.");
        }

        // 2. 새로운 비밀번호를 인코딩하여 저장
        String newPassword = request.getNewPassword();
        String encodedPassword = passwordEncoder.encode(newPassword);
        userService.updatePassword(userEmail, encodedPassword);

        // 3. 성공적으로 비밀번호를 변경한 응답 반환
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }
}
