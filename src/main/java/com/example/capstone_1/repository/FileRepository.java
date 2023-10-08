package com.example.capstone_1.repository;

import com.example.capstone_1.domain.BoardType;
import com.example.capstone_1.domain.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity,Long> {
    // 게시판 타입과 게시물 번호를 기반으로 이미지를 검색하는 커스텀 쿼리
    @Query("SELECT f FROM FileEntity f WHERE f.boardType = :boardType AND "
            + "(f.freeBoard.bno = :bno OR f.bankBoard.bno = :bno OR "
            + "f.noticeBoard.bno = :bno OR f.reportBoard.bno = :bno)")
    List<FileEntity> findImagesForBoard(@Param("boardType") BoardType boardType, @Param("bno") Long bno);
}
