package com.example.capstone_1.service;

import com.example.capstone_1.domain.*;
import com.example.capstone_1.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardControllerServiceImpl implements BoardControllerService{

    @Qualifier("freeReplyServiceImpl")
    @Autowired
    private ReplyService freeReplyService;

    @Autowired
    private FreeBoardService freeBoardService;

    @Qualifier("noticeReplyServiceImpl")
    @Autowired
    private ReplyService noticeReplyService;
    @Autowired
    private NoticeBoardService noticeBoardService;

    @Qualifier("reportReplyServiceImpl")
    @Autowired
    private ReplyService reportReplyService;
    @Autowired
    private ReportBoardService reportBoardService;


    @Autowired
    private S3Service s3Service;
    @Autowired
    private FileService fileService;

    @Override
    public void removeBoard(BoardType boardType, Long bno) {
        switch (boardType) {
            case FREE:
                FreeBoard freeBoard = freeBoardService.findById(bno);
                if (freeBoard != null) {
                    List<FileEntity> imageFiles = freeBoard.getFiles();
                    if (imageFiles != null) {
                        for (FileEntity fileEntity : imageFiles) {
                            s3Service.deleteFile(fileEntity.getFileName());
                            fileService.deleteFileById(fileEntity.getId());
                        }
                    }
                    freeReplyService.removeRepliesByBoardId(bno);
                    freeBoardService.remove(bno);
                }
                break;
            case NOTICE:
                NoticeBoard noticeBoard = noticeBoardService.findById(bno);
                if (noticeBoard != null) {
                    List<FileEntity> imageFiles = noticeBoard.getFiles();
                    if (imageFiles != null) {
                        for (FileEntity fileEntity : imageFiles) {
                            s3Service.deleteFile(fileEntity.getFileName());
                            fileService.deleteFileById(fileEntity.getId());
                        }
                    }
                    noticeReplyService.removeRepliesByBoardId(bno);
                    noticeBoardService.remove(bno);
                }
                break;
            case REPORT:
                ReportBoard reportBoard = reportBoardService.findById(bno);
                if (reportBoard != null) {
                    List<FileEntity> imageFiles = reportBoard.getFiles();
                    if (imageFiles != null) {
                        for (FileEntity fileEntity : imageFiles) {
                            s3Service.deleteFile(fileEntity.getFileName());
                            fileService.deleteFileById(fileEntity.getId());
                        }
                    }
                }
                reportReplyService.removeRepliesByBoardId(bno);
                reportBoardService.remove(bno);
                break;

            default:
                throw new IllegalArgumentException("Invalid board type: " + boardType);
        }
    }

    @Override
    public Long registerBoard(BoardType boardType, BoardDTO boardDTO) {

        switch (boardType) {
            case FREE:
                return freeBoardService.register(boardDTO);
            case NOTICE:
                return noticeBoardService.register(boardDTO);
            case REPORT:
                return reportBoardService.register(boardDTO);
            default:
                throw new IllegalArgumentException("Invalid board type: " + boardType);
        }
    }


    @Override
    public void modifyBoard(BoardType boardType, Long bno, BoardDTO boardDTO) {
        switch (boardType) {
            case FREE:
                FreeBoard freeBoard = freeBoardService.findById(bno);
                if (freeBoard != null) {
                    freeBoardService.modify(freeBoard, boardDTO);
                }
                break;
            case NOTICE:
                NoticeBoard noticeBoard = noticeBoardService.findById(bno);
                if (noticeBoard != null) {
                    noticeBoardService.modify(noticeBoard, boardDTO);
                }
                break;
            case REPORT:
                ReportBoard reportBoard = reportBoardService.findById(bno);
                if (reportBoard != null) {
                    reportBoardService.modify(reportBoard, boardDTO);
                }
                break;

            default:
                throw new IllegalArgumentException("Invalid board type: " + boardType);
        }
    }

    @Override
    public PageResponseDTO<BoardListReplyCountDTO> searchBoards(String boardType, PageRequestDTO pageRequestDTO) {
        if ("FREE".equals(boardType)) {
            return freeBoardService.listWithReplyCount(pageRequestDTO);
        } else if ("NOTICE".equals(boardType)) {
            return noticeBoardService.listWithReplyCount(pageRequestDTO);
        }  else if ("REPORT".equals(boardType)) {
            return reportBoardService.listWithReplyCount(pageRequestDTO);
        } else {
            throw new IllegalArgumentException("Invalid board type: " + boardType);
        }
    }

    @Override
    public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(String boardType, PageRequestDTO pageRequestDTO) {
        if ("FREE".equals(boardType)) {
            return freeBoardService.listWithReplyCount(pageRequestDTO);
        } else if ("NOTICE".equals(boardType)) {
            return noticeBoardService.listWithReplyCount(pageRequestDTO);
        }else if ("REPORT".equals(boardType)) {
                return reportBoardService.listWithReplyCount(pageRequestDTO);
        } else {
            throw new IllegalArgumentException("Invalid board type: " + boardType);
        }
    }

    @Override
    public Board_File_DTO getBoardWithImages(BoardType boardType, Long bno) {
        Board_File_DTO boardWithImages;
        switch (boardType) {
            case FREE:
                boardWithImages = freeBoardService.read(boardType, bno);
                return boardWithImages;
            case REPORT:
                boardWithImages = reportBoardService.read(boardType, bno);
                return boardWithImages;
            case NOTICE:
                boardWithImages = noticeBoardService.read(boardType, bno);
                return boardWithImages;
            default:
                throw new IllegalArgumentException("Invalid board type: " + boardType);
        }
    }
}
