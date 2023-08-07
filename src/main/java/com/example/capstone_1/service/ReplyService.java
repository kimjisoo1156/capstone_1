package com.example.capstone_1.service;

import com.example.capstone_1.dto.PageRequestDTO;
import com.example.capstone_1.dto.PageResponseDTO;
import com.example.capstone_1.dto.ReplyDTO;

public interface ReplyService {

    Long register(ReplyDTO replyDTO);

    ReplyDTO read(Long rno);

    void modify(ReplyDTO replyDTO);

    void remove(Long rno);

    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);

    void removeRepliesByBoardId(Long bno);


}
