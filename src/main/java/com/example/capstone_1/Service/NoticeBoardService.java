package com.example.capstone_1.service;


import com.example.capstone_1.domain.NoticeBoard;
import com.example.capstone_1.dto.BoardDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface NoticeBoardService extends BoardService {

    default NoticeBoard dtoToEntityNoticeBoard(BoardDTO boardDTO){

        NoticeBoard board = NoticeBoard.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())
                .build();

        if(boardDTO.getFileNames() != null){
            boardDTO.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_");
                board.addImageNoticeBoard(arr[0], arr[1]);
            });
        }
        return board;
    }

    default BoardDTO entityToDTONoticeBoard(NoticeBoard board) {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        List<String> fileNames =
                board.getImageSetNoticeBoard().stream().sorted().map(boardImage ->
                        boardImage.getUuid()+"_"+boardImage.getFileName()).collect(Collectors.toList());

        boardDTO.setFileNames(fileNames);

        return boardDTO;
    }
}