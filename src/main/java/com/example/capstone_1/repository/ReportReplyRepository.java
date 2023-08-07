package org.zerock.b01.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.b01.domain.FreeReply;
import org.zerock.b01.domain.ReportReply;

public interface ReportReplyRepository extends JpaRepository<ReportReply, Long> {

    @Query("select r from ReportReply r where r.reportBoard.bno = :bno")
    Page<ReportReply> listOfBoardReportReply(Long bno, Pageable pageable);

    void deleteByReportBoard_Bno(Long bno);
}
