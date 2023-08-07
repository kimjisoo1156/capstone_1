package org.zerock.b01.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.b01.domain.FreeBoard;
import org.zerock.b01.domain.ReportBoard;

public interface ReportBoardSearch extends BoardSearch{
    Page<ReportBoard> search1(Pageable pageable);

    Page<ReportBoard> searchAll(String[] types, String keyword, Pageable pageable);
}
