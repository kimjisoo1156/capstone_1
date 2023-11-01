package com.example.capstone_1.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardListReplyCountDTO {

    private Long bno;

    private String title;

    private String writer;

    private LocalDateTime regDate;

    private String secret;

    private Long replyCount;

}
