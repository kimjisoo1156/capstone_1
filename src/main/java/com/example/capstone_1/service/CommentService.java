package com.example.capstone_1.service;

import com.example.capstone_1.dto.PageRequestDTO;
import com.example.capstone_1.dto.PageResponseDTO;
import com.example.capstone_1.dto.ReplyDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final ReplyService freeReplyService;
//    private final ReplyService bankReplyService;
    private final ReplyService noticeReplyService;
    private final ReplyService reportReplyService;

    public CommentService(@Qualifier("freeReplyServiceImpl") ReplyService freeReplyService,
//                          @Qualifier("bankReplyServiceImpl")ReplyService bankReplyService,
                          @Qualifier("noticeReplyServiceImpl")ReplyService noticeReplyService,
                          @Qualifier("reportReplyServiceImpl")ReplyService reportReplyService) {
        this.freeReplyService = freeReplyService;
//        this.bankReplyService = bankReplyService;
        this.noticeReplyService = noticeReplyService;
        this.reportReplyService = reportReplyService;
    }

    public Long register(String boardType, ReplyDTO commentDTO) {
        // 게시판 타입에 따라 적절한 댓글 등록 로직 수행
        if ("FREE".equals(boardType)) {
            return freeReplyService.register(commentDTO);
        }else if ("NOTICE".equals(boardType)) {
            return noticeReplyService.register(commentDTO);
        }else if ("REPORT".equals(boardType)){
            return reportReplyService.register(commentDTO);
        } else {
            throw new IllegalArgumentException("Invalid board type: " + boardType);
        }
    }

    public ReplyDTO read(String boardType, Long rno) {
        // 게시판 타입에 따라 적절한 댓글 조회 로직 수행
        if ("FREE".equals(boardType)) {
            return freeReplyService.read(rno);
        }else if ("NOTICE".equals(boardType)) {
            return noticeReplyService.read(rno);
        } else if ("REPORT".equals(boardType)) {
            return reportReplyService.read(rno);
        } else {
            throw new IllegalArgumentException("Invalid board type: " + boardType);
        }
    }
    public void remove(String boardType, Long rno) {
        // 게시판 타입에 따라 적절한 댓글 삭제 로직 수행
        if ("FREE".equals(boardType)) {
            freeReplyService.remove(rno);
        } else if ("NOTICE".equals(boardType)) {
            noticeReplyService.remove(rno);
        } else if ("REPORT".equals(boardType)) {
            reportReplyService.remove(rno);
        } else {
            throw new IllegalArgumentException("Invalid board type: " + boardType);
        }
    }

    public void modify(String boardType, ReplyDTO commentDTO) {
        // 게시판 타입에 따라 적절한 댓글 수정 로직 수행
        if ("FREE".equals(boardType)) {
            freeReplyService.modify(commentDTO);
        } else if ("NOTICE".equals(boardType)) {
            noticeReplyService.modify(commentDTO);
        } else if ("REPORT".equals(boardType)) {
            reportReplyService.modify(commentDTO);
        } else {
            throw new IllegalArgumentException("Invalid board type: " + boardType);
        }
    }
    public PageResponseDTO<ReplyDTO> getListOfBoard(String boardType, Long bno, PageRequestDTO pageRequestDTO) {
        // 게시판 타입에 따라 적절한 댓글 목록 조회 로직 수행
        if ("FREE".equals(boardType)) {
            return freeReplyService.getListOfBoard(bno, pageRequestDTO);
        } else if ("NOTICE".equals(boardType)) {
            return noticeReplyService.getListOfBoard(bno, pageRequestDTO);
        } else if ("REPORT".equals(boardType)) {
            return reportReplyService.getListOfBoard(bno, pageRequestDTO);
        }else {
            throw new IllegalArgumentException("Invalid board type: " + boardType);
        }
    }



}
