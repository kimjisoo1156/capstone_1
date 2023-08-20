package com.example.capstone_1.service;

import com.example.capstone_1.domain.BoardType;
import com.example.capstone_1.dto.BankBoardDTO;
import com.example.capstone_1.dto.BoardDTO;

public interface BoardControllerService {
    void removeBoard(BoardType boardType, Long bno);
    Long registerBoard(BoardType boardType, BoardDTO boardDTO);

    Long registerBankBoard(BoardType boardType, BankBoardDTO bankBoardDTO);
    void modifyBoard(BoardType boardType, Long bno, BoardDTO boardDTO);

}
