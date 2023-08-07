package org.zerock.b01.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.b01.domain.BankReply;
import org.zerock.b01.domain.FreeReply;
import org.zerock.b01.domain.NoticeReply;
import org.zerock.b01.domain.ReportReply;

public interface NoticeReplyRepository extends JpaRepository<NoticeReply, Long> {
    @Query("select r from NoticeReply r where r.noticeBoard.bno = :bno")
    Page<NoticeReply> listOfBoardNoticeReply(Long bno, Pageable pageable);

    void deleteByNoticeBoard_Bno(Long bno);
}
