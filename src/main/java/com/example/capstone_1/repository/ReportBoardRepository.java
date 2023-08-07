package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.b01.domain.ReportBoard;
import org.zerock.b01.repository.search.ReportBoardSearch;

import java.util.Optional;

public interface ReportBoardRepository extends JpaRepository<ReportBoard, Long>, ReportBoardSearch {

    @EntityGraph(attributePaths = {"imageSetReportBoard"})
    @Query("select b from ReportBoard b where b.bno =:bno")
    Optional<ReportBoard> findByIdWithImagesReportBoard(Long bno);
}
