package com.example.capstone_1.repository.search;

import com.example.capstone_1.domain.BankBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BankBoardSearch extends BoardSearch{

    Page<BankBoard> search1(Pageable pageable);

    Page<BankBoard> searchAll(String[] types, String keyword, Pageable pageable);
}
