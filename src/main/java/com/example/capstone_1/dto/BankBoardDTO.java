//package com.example.capstone_1.dto;
//
//import com.example.capstone_1.domain.BoardType;
//import jakarta.validation.constraints.NotBlank;
//import lombok.*;
//import lombok.experimental.SuperBuilder;
//import org.modelmapper.internal.bytebuddy.implementation.bind.annotation.Super;
//
//import java.time.LocalDateTime;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@SuperBuilder
//public class BankBoardDTO extends BoardDTO {
//
//    @NotBlank
//    private String accountHolder; // 예금주
//
//    @NotBlank
//    private String bankName; // 은행이름
//
//    @NotBlank
//    private String accountNumber; // 계좌번호
//
//
//    // 상위 클래스의 필드를 사용하기 위해 오버라이드
//    @Override
//    public BoardType getBoardType() {
//        return super.getBoardType();
//    }
//}
