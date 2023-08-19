package com.example.capstone_1.repository;

import com.example.capstone_1.domain.BankBoard;
import com.example.capstone_1.repository.search.BankBoardSearch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BankBoardRepository extends JpaRepository<BankBoard, Long>, BankBoardSearch {

}
