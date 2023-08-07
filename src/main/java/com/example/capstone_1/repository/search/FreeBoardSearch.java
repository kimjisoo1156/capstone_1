package org.zerock.b01.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.b01.domain.FreeBoard;

public interface FreeBoardSearch extends BoardSearch{
    Page<FreeBoard> search1(Pageable pageable);

    Page<FreeBoard> searchAll(String[] types, String keyword, Pageable pageable);
}
