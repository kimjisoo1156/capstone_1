package org.zerock.b01.service;

import org.zerock.b01.domain.BankReply;
import org.zerock.b01.dto.ReplyDTO;

import java.util.List;

public interface BankReplyService extends ReplyService{
    // 댓글의 내용을 기준으로 댓글을 조회하는 메서드
    List<ReplyDTO> findByReplyText(String replyText);
}
