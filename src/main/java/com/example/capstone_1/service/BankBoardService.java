//package com.example.capstone_1.service;
//
//import com.example.capstone_1.domain.BankBoard;
//import com.example.capstone_1.dto.*;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//public interface BankBoardService {
//
//    Long register(BankBoardDTO boardDTO);
//
//    BankBoardDTO readOne(Long bno);
//
//    void modify(BankBoardDTO boardDTO);
//
//    void remove(Long bno);
//
//    PageResponseDTO<BankBoardDTO> list(PageRequestDTO pageRequestDTO);
//
//    //댓글의 숫자까지 처리
//    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);
//
//    //게시글의 이미지와 댓글의 숫자까지 처리
//    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);
//
//
//    default BankBoard dtoToEntityBankBoard(BankBoardDTO boardDTO){
//
//        BankBoard board = BankBoard.builder()
//                .bno(boardDTO.getBno())
//                .title(boardDTO.getTitle())
//                .accountHolder(boardDTO.getAccountHolder())
//                .bankName(boardDTO.getBankName())
//                .accountNumber(boardDTO.getAccountNumber())
//                .content(boardDTO.getContent())
//                .writer(boardDTO.getWriter())
//
//                .build();
//
//        if(boardDTO.getFileNames() != null){
//            boardDTO.getFileNames().forEach(fileName -> {
//                String[] arr = fileName.split("_");
//                board.addImageBankBoard(arr[0], arr[1]);
//            });
//        }
//        return board;
//    }
//
//    default BankBoardDTO entityToDTOBankBoard(BankBoard board) {
//
//        BankBoardDTO bankBoardDTO = BankBoardDTO.builder()
//                .bno(board.getBno())
//                .title(board.getTitle())
//                .accountHolder(board.getAccountHolder())
//                .bankName(board.getBankName())
//                .accountNumber(board.getAccountNumber())
//                .content(board.getContent())
//                .writer(board.getWriter())
//                .regDate(board.getRegDate())
//                .modDate(board.getModDate())
//                .build();
//
//        List<String> fileNames =
//                board.getImageSetBankBoard().stream().sorted().map(boardImage ->
//                        boardImage.getUuid()+"_"+boardImage.getFileName()).collect(Collectors.toList());
//
//        bankBoardDTO.setFileNames(fileNames);
//
//        return bankBoardDTO;
//    }
//}
