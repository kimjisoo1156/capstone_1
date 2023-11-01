package com.example.capstone_1.service;

import com.example.capstone_1.domain.BoardType;
import com.example.capstone_1.dto.*;

public interface BoardControllerService {
    void removeBoard(BoardType boardType, Long bno);
    Long registerBoard(BoardType boardType, BoardDTO boardDTO);

//    Long registerBankBoard(BoardType boardType, BankBoardDTO bankBoardDTO);
    void modifyBoard(BoardType boardType, Long bno, BoardDTO boardDTO);
    PageResponseDTO<BoardListReplyCountDTO> searchBoards(String boardType, PageRequestDTO pageRequestDTO);
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(String boardType, PageRequestDTO pageRequestDTO);
    Board_File_DTO getBoardWithImages(BoardType boardType, Long bno);


}
