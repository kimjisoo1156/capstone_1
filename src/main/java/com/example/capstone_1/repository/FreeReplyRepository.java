package com.example.capstone_1.repository;

import com.example.capstone_1.domain.FreeReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FreeReplyRepository extends JpaRepository<FreeReply, Long> {

    @Query("select r from FreeReply r where r.freeBoard.bno = :bno")
    Page<FreeReply> listOfBoardFreeReply(@Param("bno")Long bno, Pageable pageable);

    void deleteByFreeBoard_Bno(Long bno);

    // 작성자(replyer)로 댓글 찾기
    List<FreeReply> findByReplyer(String replyer);
}
