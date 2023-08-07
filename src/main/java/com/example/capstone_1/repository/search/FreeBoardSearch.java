package com.example.capstone_1.repository.search;

import com.example.capstone_1.domain.FreeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FreeBoardSearch extends BoardSearch{
    Page<FreeBoard> search1(Pageable pageable);

    Page<FreeBoard> searchAll(String[] types, String keyword, Pageable pageable);
}
