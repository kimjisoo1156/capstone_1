package com.example.capstone_1.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.modelmapper.internal.bytebuddy.implementation.bind.annotation.Super;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BankBoardDTO extends BoardDTO {

    @NotBlank
    private String accountHolder; // 예금주

    @NotBlank
    private String bankName; // 은행이름

    @NotBlank
    private String accountNumber; // 계좌번호

}
