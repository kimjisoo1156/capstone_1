package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.b01.domain.FreeBoard;
import org.zerock.b01.repository.search.BoardSearch;
import org.zerock.b01.repository.search.FreeBoardSearch;

import java.util.Optional;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long>, FreeBoardSearch {

    @EntityGraph(attributePaths = {"imageSetFreeBoard"})
    @Query("select b from FreeBoard b where b.bno =:bno")
    Optional<FreeBoard> findByIdWithImagesFreeBoard(Long bno);

}
