package com.example.capstone_1.service;

import com.example.capstone_1.domain.FreeBoard;
import com.example.capstone_1.dto.BoardDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public interface FreeBoardService extends BoardService{

    List<String> uploadImagesToS3(List<MultipartFile> images);

    String uploadImageToS3(MultipartFile image);

    default FreeBoard dtoToEntityFreeBoard(BoardDTO boardDTO){

        FreeBoard board = FreeBoard.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())

                .build();

        if(boardDTO.getFileNames() != null){
            boardDTO.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_");
                board.addImageFreeBoard(arr[0], arr[1]);
            });
        }
        return board;
    }

    default BoardDTO entityToDTOFreeBoard(FreeBoard board) {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        List<String> fileNames =
                board.getImageSetFreeBoard().stream().sorted().map(boardImage ->
                        boardImage.getUuid()+"_"+boardImage.getImageUrl()).collect(Collectors.toList());

        boardDTO.setFileNames(fileNames);

        return boardDTO;
    }
}
