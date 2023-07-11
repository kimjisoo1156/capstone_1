package com.example.capstone_1.Entity;


import jakarta.persistence.*;
import lombok.*;

@Table(name="Member")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
@Entity(name="Member")
public class MemberEnity {
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


}
