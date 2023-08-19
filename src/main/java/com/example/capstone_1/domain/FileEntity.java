package com.example.capstone_1.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String fileName;

    @Column
    private String uuid;

    @Column
    private String s3Url;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // Enum 타입을 문자열로 저장
    private BoardType boardType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "free_board_id") // Board 엔터티와의 관계를 나타내는 필드
    private FreeBoard freeBoard; //자유게시판 번호


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_board_id")
    private BankBoard bankBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_board_id")
    private NoticeBoard noticeBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_board_id")
    private ReportBoard reportBoard;

    public FileEntity(String fileName, String uuid, String s3Url, BoardType boardType,
                      FreeBoard freeBoard, BankBoard bankBoard, NoticeBoard noticeBoard,
                      ReportBoard reportBoard) {
        this.fileName = fileName;
        this.uuid = uuid;
        this.s3Url = s3Url;
        this.boardType = boardType;
        this.freeBoard = freeBoard;
        this.bankBoard = bankBoard;
        this.noticeBoard = noticeBoard;
        this.reportBoard = reportBoard;

    }
}
