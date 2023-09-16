package com.example.capstone_1.domain;

import lombok.*;
import jakarta.persistence.*;


@Entity
@Table(name = "FreeReply", indexes = {
        @Index(name = "idx_reply_board_bno", columnList = "free_board_bno")
})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@ToString(exclude = "board")
//@ToString
public class FreeReply extends BaseEntity{  //자유게시판 댓글

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    @ManyToOne(fetch = FetchType.LAZY)
    private FreeBoard freeBoard;

    private String replyText;

    private String replyer;

    public void changeTextFreeReply(String text){

        this.replyText = text;
    }
    public void setReplyerToUnknown() {
        this.replyer = "알수없음";
    }
}


