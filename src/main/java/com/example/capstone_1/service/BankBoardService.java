//package com.example.capstone_1.service;
//
//import com.example.capstone_1.domain.BankBoard;
//import com.example.capstone_1.domain.BoardType;
//import com.example.capstone_1.domain.FreeBoard;
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
//    Board_File_DTO read(BoardType boardtype, Long bno);
//    //댓글의 숫자까지 처리
//    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);
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
//                .secret(boardDTO.getSecret())
//                .build();
//
//
//        return board;
//    }
//    default BankBoard findById(Long bno) {
//        return null;
//    }
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
//                .secret(board.getSecret())
//                .build();
//
//
//
//        return bankBoardDTO;
//    }
//}
