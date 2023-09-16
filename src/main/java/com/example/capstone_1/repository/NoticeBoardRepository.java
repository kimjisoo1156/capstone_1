package com.example.capstone_1.repository;

import com.example.capstone_1.domain.FreeBoard;
import com.example.capstone_1.domain.NoticeBoard;
import com.example.capstone_1.repository.search.NoticeBoardSearch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Long>, NoticeBoardSearch {
    List<NoticeBoard> findByWriter(String writer);
}
