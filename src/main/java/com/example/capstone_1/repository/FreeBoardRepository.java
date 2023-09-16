package com.example.capstone_1.repository;

import com.example.capstone_1.domain.FreeBoard;
import com.example.capstone_1.repository.search.FreeBoardSearch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long>, FreeBoardSearch {
    // 작성자(email)로 게시물 찾기
    List<FreeBoard> findByWriter(String writer);

}
