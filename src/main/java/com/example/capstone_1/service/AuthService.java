package com.example.capstone_1.service;

import com.example.capstone_1.domain.Member;
import com.example.capstone_1.domain.RefreshToken;
import com.example.capstone_1.dto.*;
import com.example.capstone_1.jwt.TokenProvider;
import com.example.capstone_1.repository.MemberRepository;
import com.example.capstone_1.repository.RefreshTokenRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.internal.util.Members;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    //@Transactional
    public MemberResponseDto signup(MemberRequestDto memberRequestDto) {

        if (memberRepository.existsByEmail(memberRequestDto.getEmail())) {
           throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        Member member = memberRequestDto.toMember(passwordEncoder);
        return MemberResponseDto.of(memberRepository.save(member));
    }


    public void validateSignUpRequest(MemberRequestDto memberRequestDto) {
        // 간단한 유효성 검사 예시
        if (!isValidEmail(memberRequestDto.getEmail())) {
            throw new IllegalArgumentException("Invalid email address");
        }

        if (!isValidPassword(memberRequestDto.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        if (!isValidPhoneNumber(memberRequestDto.getPhoneNumber())) {
            throw new IllegalArgumentException("전화번호 형식은");
        }
    }

    // 실제 회원가입 로직
    private boolean isValidEmail(String email) {
        // 이메일 유효성 검사 로직
        return email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$");
    }

    private boolean isValidPassword(String password) {
        // 비밀번호 유효성 검사 로직
        return password.matches("^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$");
       // return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()-_+=]).{8,}$");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // 전화번호 유효성 검사 로직
        return phoneNumber.matches("^010-[0-9]{4}-[0-9]{4}$");
    }



    public class CustomAuthenticationException extends AuthenticationException {
        public CustomAuthenticationException(String message) {
            super(message);
        }
    }
   // @Transactional
    public TokenDto login(LoginDto loginDto) { //로그인
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication;

        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            // 로그인 실패 시 예외 처리
            return null;
        }

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return tokenDto;
    }

//    @Transactional
//    public TokenDto login(LoginDto loginDto) { //로그인
//
//            // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
//            UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthentication();
//
//            // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
//            //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
//            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//
//            // 3. 인증 정보를 기반으로 JWT 토큰 생성
//            TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
//
//            // 4. RefreshToken 저장
//            RefreshToken refreshToken = RefreshToken.builder()
//                    .key(authentication.getName())
//                    .value(tokenDto.getRefreshToken())
//                    .build();
//
//            refreshTokenRepository.save(refreshToken);
//
//            // 5. 토큰 발급
//            return tokenDto;
//
//
//    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }

    // 이메일로 비밀번호 새로 발급해줄때
    @Transactional
    public void SetTempPassword(String email, String newPassword) {
        memberService.updatePassword(email, newPassword);
    }



}
