package com.example.capstone_1.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {
    private String memberEmail;
    private String memberPassword;
    private String memberPasswordCheck;
    private String memberPhoneNumber;
    private String memberName;
    private String memberBankName;
    private String memberAccount;
    // private Role memberRole;
}
