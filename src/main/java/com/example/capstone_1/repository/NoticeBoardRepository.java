package com.example.capstone_1.repository;

import com.example.capstone_1.domain.NoticeBoard;
import com.example.capstone_1.repository.search.NoticeBoardSearch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Long>, NoticeBoardSearch {
    @EntityGraph(attributePaths = {"imageSetNoticeBoard"})
    @Query("select b from NoticeBoard b where b.bno =:bno")
    Optional<NoticeBoard> findByIdWithImagesNoticeBoard(Long bno);
}
