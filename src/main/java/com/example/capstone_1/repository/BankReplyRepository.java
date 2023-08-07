package com.example.capstone_1.repository;

import com.example.capstone_1.domain.BankReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BankReplyRepository extends JpaRepository<BankReply, Long> {

    @Query("select r from BankReply r where r.bankBoard.bno = :bno")

    Page<BankReply> listOfBoardBankReply(Long bno, Pageable pageable);

    void deleteByBankBoard_Bno(Long bno);

    // 댓글의 내용을 기준으로 댓글을 조회하는 메서드
    List<BankReply> findByReplyText(String replyText);

}
