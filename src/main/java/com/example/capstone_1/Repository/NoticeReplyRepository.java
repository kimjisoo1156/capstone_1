package com.example.capstone_1.repository;

import com.example.capstone_1.domain.NoticeReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NoticeReplyRepository extends JpaRepository<NoticeReply, Long> {
    @Query("select r from NoticeReply r where r.noticeBoard.bno = :bno")
    Page<NoticeReply> listOfBoardNoticeReply(Long bno, Pageable pageable);

    void deleteByNoticeBoard_Bno(Long bno);
}