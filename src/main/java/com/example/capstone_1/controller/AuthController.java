package com.example.capstone_1.controller;

import com.example.capstone_1.dto.*;
import com.example.capstone_1.jwt.TokenProvider;
import com.example.capstone_1.repository.MemberRepository;
import com.example.capstone_1.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final MemberRepository memberRepository;

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto memberRequestDto){

        return ResponseEntity.ok(authService.signup(memberRequestDto));
    }

    @GetMapping(value ="/check-email",produces="application/json; charset=utf8")
    public ResponseEntity<String> checkEmailDuplication(@RequestParam String email) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("이메일을 입력하세요.");
        }

        boolean isEmailDuplicate = memberRepository.existsByEmail(email);

        if (isEmailDuplicate) {
            return ResponseEntity.ok("중복된 이메일입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 이메일입니다.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        TokenDto tokenDto = authService.login(loginDto);

        if (tokenDto != null) {
            // 로그인 성공 시
            return ResponseEntity.ok(tokenDto);
        } else {
            // 로그인 실패 시
            return ResponseEntity.badRequest().body("login failed");
        }
    }


    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }










}
