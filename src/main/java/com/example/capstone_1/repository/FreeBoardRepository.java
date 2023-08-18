package com.example.capstone_1.repository;

import com.example.capstone_1.domain.FreeBoard;
import com.example.capstone_1.repository.search.FreeBoardSearch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long>, FreeBoardSearch {


}
