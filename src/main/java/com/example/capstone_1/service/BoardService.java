package com.example.capstone_1.service;


import com.example.capstone_1.domain.BoardType;
import com.example.capstone_1.dto.*;

public interface BoardService {

    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);
    Board_File_DTO read(BoardType boardtype, Long bno);
//    void modify(BoardDTO boardDTO);

    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    //댓글의 숫자까지 처리
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);



}
