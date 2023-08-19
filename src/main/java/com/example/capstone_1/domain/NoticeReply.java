package com.example.capstone_1.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aspectj.weaver.ast.Not;

@Entity
@Table(name = "NoticeReply", indexes = {
        @Index(name = "idx_reply_board_bno", columnList = "notice_board_bno")
})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeReply extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    @ManyToOne(fetch = FetchType.LAZY)
    private NoticeBoard noticeBoard;

    private String replyText;

    private String replyer;

    public void changeTextNoticeReply(String text){

        this.replyText = text;
    }
}
