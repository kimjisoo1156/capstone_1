package com.example.capstone_1.service;

import com.example.capstone_1.domain.FreeBoard;
import com.example.capstone_1.dto.BoardDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface FreeBoardService extends BoardService{

    default FreeBoard dtoToEntityFreeBoard(BoardDTO boardDTO){

        FreeBoard board = FreeBoard.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())
                .secret(boardDTO.getSecret())
                .build();

        return board;
    }
    default FreeBoard findById(Long bno) {
        return null;
    }

    void modify(FreeBoard freeBoard, BoardDTO boardDTO);
    default BoardDTO entityToDTOFreeBoard(FreeBoard board) {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .secret(board.getSecret())
                .build();

        return boardDTO;
    }
}
