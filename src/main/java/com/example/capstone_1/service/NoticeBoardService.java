package com.example.capstone_1.service;



import com.example.capstone_1.domain.FreeBoard;
import com.example.capstone_1.domain.NoticeBoard;
import com.example.capstone_1.dto.BoardDTO;


public interface NoticeBoardService extends BoardService {

    default NoticeBoard dtoToEntityNoticeBoard(BoardDTO boardDTO){

        NoticeBoard board = NoticeBoard.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())
                .secret(boardDTO.getSecret())
                .build();

        return board;
    }
    void modify(NoticeBoard noticeBoard, BoardDTO boardDTO);
    default BoardDTO entityToDTONoticeBoard(NoticeBoard board) {

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
    default NoticeBoard findById(Long bno) {
        return null;
    }
}
