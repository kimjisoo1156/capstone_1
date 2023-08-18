package com.example.capstone_1.repository.search;

import com.example.capstone_1.dto.BoardListReplyCountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {

    Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types,
                                                      String keyword,
                                                      Pageable pageable);
//
//    Page<BoardListAllDTO> searchWithAll(String[] types,
//                                        String keyword,
//                                        Pageable pageable);



}
