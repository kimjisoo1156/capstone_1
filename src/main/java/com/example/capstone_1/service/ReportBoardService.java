package com.example.capstone_1.service;


import com.example.capstone_1.domain.FreeBoard;
import com.example.capstone_1.domain.ReportBoard;
import com.example.capstone_1.dto.BoardDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface ReportBoardService extends BoardService{

    default ReportBoard dtoToEntityReportBoard(BoardDTO boardDTO){

        ReportBoard board = ReportBoard.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())
                .secret(boardDTO.getSecret())
                .build();

        return board;
    }

    default BoardDTO entityToDTOReportBoard(ReportBoard board) {

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
    default ReportBoard findById(Long bno) {
        return null;
    }
}
