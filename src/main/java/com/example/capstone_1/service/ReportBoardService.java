package org.zerock.b01.service;

import org.zerock.b01.domain.ReportBoard;
import org.zerock.b01.dto.BoardDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface ReportBoardService extends BoardService{

    default ReportBoard dtoToEntityReportBoard(BoardDTO boardDTO){

        ReportBoard board = ReportBoard.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())

                .build();

        if(boardDTO.getFileNames() != null){
            boardDTO.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_");
                board.addImageRepoartBoard(arr[0], arr[1]);
            });
        }
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
                .build();

        List<String> fileNames =
                board.getImageSetReportBoard().stream().sorted().map(boardImage ->
                        boardImage.getUuid()+"_"+boardImage.getFileName()).collect(Collectors.toList());

        boardDTO.setFileNames(fileNames);

        return boardDTO;
    }
}
