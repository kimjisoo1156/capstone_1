package com.example.capstone_1.service;


import com.example.capstone_1.dto.*;

public interface BoardService {

    Long register(BoardDTO boardDTO,String imagePath);

    BoardDTO readOne(Long bno);

    void modify(BoardDTO boardDTO,String imagePath);

    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    //댓글의 숫자까지 처리
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

    //게시글의 이미지와 댓글의 숫자까지 처리
    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);




}
