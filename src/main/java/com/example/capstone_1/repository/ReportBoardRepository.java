package com.example.capstone_1.repository;

import com.example.capstone_1.domain.FreeBoard;
import com.example.capstone_1.domain.ReportBoard;
import com.example.capstone_1.repository.search.ReportBoardSearch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReportBoardRepository extends JpaRepository<ReportBoard, Long>, ReportBoardSearch {
    List<ReportBoard> findByWriter(String writer);
}
