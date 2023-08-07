package org.zerock.b01.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.b01.domain.NoticeBoard;

public interface NoticeBoardSearch extends BoardSearch{

    Page<NoticeBoard> search1(Pageable pageable);

    Page<NoticeBoard> searchAll(String[] types, String keyword, Pageable pageable);
}
