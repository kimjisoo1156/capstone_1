package com.example.capstone_1.domain;

import lombok.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@ToString(exclude = "board")
public class BoardImage implements Comparable<BoardImage> {  //자유게시판, 신고게시판 이미지 첨부

    @Id
    private String uuid;

    private String fileName;

    private String imageUrl;

    private int ord;

    @ManyToOne
    private FreeBoard freeBoard;

    @ManyToOne
    private ReportBoard reportBoard;

    @ManyToOne
    private BankBoard bankBoard;

    @ManyToOne
    private NoticeBoard noticeBoard;

    @Override
    public int compareTo(BoardImage other) {
        return this.ord - other.ord;
    }

    public void changeBoardFreeBoard(FreeBoard freeBoard){
        this.freeBoard = freeBoard;
    }
    public void changeBoardReportBoard(ReportBoard reportBoard){
        this.reportBoard = reportBoard;
    }
    public void changeBoardBankBoard(BankBoard bankBoard){
        this.bankBoard = bankBoard;
    }
    public void changeBoardNoticeBoard(NoticeBoard noticeBoard){
        this.noticeBoard = noticeBoard;
    }

}
