package com.example.capstone_1.repository.search;

import com.example.capstone_1.domain.ReportBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportBoardSearch extends BoardSearch{
    Page<ReportBoard> search1(Pageable pageable);

    Page<ReportBoard> searchAll(String[] types, String keyword, Pageable pageable);
}
