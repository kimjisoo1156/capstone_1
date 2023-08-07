package org.zerock.b01.repository.search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.b01.domain.BankBoard;

public interface BankBoardSearch extends BoardSearch{

    Page<BankBoard> search1(Pageable pageable);

    Page<BankBoard> searchAll(String[] types, String keyword, Pageable pageable);
}
