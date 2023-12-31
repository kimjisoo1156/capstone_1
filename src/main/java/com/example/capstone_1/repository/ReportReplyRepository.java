package com.example.capstone_1.repository;

import com.example.capstone_1.domain.FreeReply;
import com.example.capstone_1.domain.ReportReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportReplyRepository extends JpaRepository<ReportReply, Long> {

    @Query("select r from ReportReply r where r.reportBoard.bno = :bno")
    Page<ReportReply> listOfBoardReportReply(@Param("bno")Long bno, Pageable pageable);

    void deleteByReportBoard_Bno(Long bno);

    List<ReportReply> findByReplyer(String replyer);
}
