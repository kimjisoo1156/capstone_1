package com.example.capstone_1.service;


import com.example.capstone_1.dto.ReplyDTO;

import java.util.List;

public interface BankReplyService extends ReplyService{
    // 댓글의 내용을 기준으로 댓글을 조회하는 메서드
    List<ReplyDTO> findByReplyText(String replyText);
}
