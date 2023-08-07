package org.zerock.b01.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.b01.domain.FreeReply;

public interface FreeReplyRepository extends JpaRepository<FreeReply, Long> {

    @Query("select r from FreeReply r where r.freeBoard.bno = :bno")
    Page<FreeReply> listOfBoardFreeReply(Long bno, Pageable pageable);

    void deleteByFreeBoard_Bno(Long bno);
}
