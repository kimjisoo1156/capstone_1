package com.example.capstone_1.Entity;


import com.example.capstone_1.Dto.SignUpDto;
import jakarta.persistence.*;
import lombok.*;

@Table(name="Member")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
@Entity(name="Member")
public class MemberEntity {
    @Id
    @Column(name="member_email")
    private String memberEmail;

    @Column(name="member_password")
    private String memberPassword;

    @Column(name="member_phone_number")
    private String memberPhoneNumber;

    @Column(name="member_name")
    private String memberName;

    @Column(name="member_bank_name")
    private String memberBankName;

    @Column(name="member_account")
    private String memberAccount;

    @Enumerated(EnumType.STRING)
    @Column(name="member_role")
    private Role memberRole;

    public MemberEntity(SignUpDto dto){  //post 맨에서 값 입력한대로 필드 초기화 하는부분.
        this.memberEmail = dto.getMemberEmail();
        this.memberPassword = dto.getMemberPassword();
        this.memberPhoneNumber = dto.getMemberPhoneNumber();
        this.memberName = dto.getMemberName();
        this.memberBankName = dto.getMemberBankName();
        this.memberAccount = dto.getMemberAccount();
        this.memberRole = Role.USER; //회원가입은 모두 USER로
    }

}
