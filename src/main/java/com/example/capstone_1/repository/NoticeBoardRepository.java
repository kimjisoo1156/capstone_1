package com.example.capstone_1.repository;

import com.example.capstone_1.domain.NoticeBoard;
import com.example.capstone_1.repository.search.NoticeBoardSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Long>, NoticeBoardSearch {

}
