package com.example.capstone_1.Service;

import com.example.capstone_1.Dto.ResponseDto;
import com.example.capstone_1.Dto.SignInDto;
import com.example.capstone_1.Dto.SignInResponseDto;
import com.example.capstone_1.Dto.SignUpDto;
import com.example.capstone_1.Entity.MemberEntity;
import com.example.capstone_1.Repository.MemberRepository;
import com.example.capstone_1.Security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ResponseDto<?> signUp(SignUpDto dto) {
        String memberEmail = dto.getMemberEmail();
        String memberPassword = dto.getMemberPassword();
        String memberPasswordCheck = dto.getMemberPasswordCheck();


        try{
            if (memberRepository.existsById(memberEmail))
                return ResponseDto.setFailed("Existed Email!");  //아이디 중복체크
        }catch (Exception e){
            return ResponseDto.setFailed("Data Base Error!");
        }


        //비밀번호가 서로 다른면 failed response 반환!
        if (!memberPassword.equals(memberPasswordCheck)) //비밀번호와 비밀번호확인 서로 다르면 나오는 에러
            return ResponseDto.setFailed("Password does not matched");

        //UserEntity 생성
        MemberEntity memberEntity = new MemberEntity(dto);

        //비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(memberPassword);
        memberEntity.setMemberPassword(encodedPassword);

        try{
            memberRepository.save(memberEntity);
        }catch (Exception e){
            return ResponseDto.setFailed("Data Base Error!");
        }
        return ResponseDto.setSucces("Sign Up Success!", null);
    }


    public ResponseDto<SignInResponseDto> signIn(SignInDto dto){
        String memberEmail = dto.getMemberEmail();
        String memberPassword = dto.getMemberPassword();
        //String memberRole = dto.getMemberRole();

        MemberEntity memberEntity = null;
        try{
            memberEntity = memberRepository.findByMemberEmail(memberEmail);
            //잘못된 이메일
            if(memberEntity == null)return ResponseDto.setFailed("Sign In Failed");


            //if (memberRole.equals("ADMIN")){
            //잘못된 패스워드
            //      if (!memberPassword.equals(memberEntity.getMemberPassword())) {
            //         return ResponseDto.setFailed("Sign In Failed");
            //   }

            //}else{
            if(!passwordEncoder.matches(memberPassword,memberEntity.getMemberPassword()))
                return ResponseDto.setFailed("Sign In Failed");
            //}


        }catch (Exception error){
            return ResponseDto.setFailed("Database Error");
        }

        memberEntity.setMemberPassword("");

        String token = tokenProvider.create(memberEmail);
        int exprTime = 3600000;

        SignInResponseDto signInResponseDto = new SignInResponseDto(token, exprTime, memberEntity);
        return ResponseDto.setSucces("Sign In Success", signInResponseDto);
    }
}
