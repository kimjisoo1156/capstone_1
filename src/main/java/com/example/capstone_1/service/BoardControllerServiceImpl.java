package com.example.capstone_1.service;

import com.example.capstone_1.domain.*;
import com.example.capstone_1.dto.BankBoardDTO;
import com.example.capstone_1.dto.BoardDTO;
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

    @Qualifier("bankReplyServiceImpl")
    @Autowired
    private ReplyService bankReplyService;
    @Autowired
    private BankBoardService bankBoardService;

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
            case BANK:
                BankBoard bankBoard = bankBoardService.findById(bno);
                if (bankBoard != null) {
                    List<FileEntity> imageFiles = bankBoard.getFiles();
                    if (imageFiles != null) {
                        for (FileEntity fileEntity : imageFiles) {
                            s3Service.deleteFile(fileEntity.getFileName());
                            fileService.deleteFileById(fileEntity.getId());
                        }
                    }
                    bankReplyService.removeRepliesByBoardId(bno);
                    bankBoardService.remove(bno);
                }
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
    public Long registerBankBoard(BoardType boardType, BankBoardDTO bankBoardDTO) {
        if (boardType == BoardType.BANK) {
            return bankBoardService.register(bankBoardDTO);
        } else {
            throw new IllegalArgumentException("Invalid board type for BankBoard: " + boardType);
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



}