//package com.example.capstone_1.domain;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "ReportReply", indexes = {
//        @Index(name = "idx_reply_board_bno", columnList = "report_board_bno")
//})
//@Getter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public class ReportReply extends BaseEntity{
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long rno;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    private ReportBoard reportBoard;
//
//    private String replyText;
//
//    private String replyer;
//
//    public void changeTextReportReply(String text){
//
//        this.replyText = text;
//    }
//}
