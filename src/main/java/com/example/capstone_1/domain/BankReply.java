//package com.example.capstone_1.domain;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "BankReply", indexes = {
//        @Index(name = "idx_reply_board_bno", columnList = "bank_board_bno")
//})
//@Getter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public class BankReply extends BaseEntity{
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long rno;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    private BankBoard bankBoard;
//
//    private String replyText;
//
//    private String replyer;
//
//    public void changeTextBankReply(String text){
//
//        this.replyText = text;
//    }
//}
