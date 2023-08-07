package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.b01.domain.BankBoard;
import org.zerock.b01.repository.search.BankBoardSearch;

import java.util.Optional;

public interface BankBoardRepository extends JpaRepository<BankBoard, Long>, BankBoardSearch {
    @EntityGraph(attributePaths = {"imageSetBankBoard"})
    @Query("select b from BankBoard b where b.bno =:bno")
    Optional<BankBoard> findByIdWithImagesBankBoard(Long bno);
}
