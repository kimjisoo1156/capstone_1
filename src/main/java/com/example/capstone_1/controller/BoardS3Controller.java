package com.example.capstone_1.controller;

import com.example.capstone_1.domain.*;
import com.example.capstone_1.dto.*;
import com.example.capstone_1.repository.BankBoardRepository;
import com.example.capstone_1.repository.FreeBoardRepository;
import com.example.capstone_1.repository.NoticeBoardRepository;
import com.example.capstone_1.repository.ReportBoardRepository;
import com.example.capstone_1.service.*;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Log4j2
@RequestMapping("/boards")
public class BoardS3Controller {
    private final FreeBoardService freeBoardService;
    private final FreeBoardRepository freeBoardRepository;
    private final ReplyService freeReplyService;

    private final NoticeBoardService noticeBoardService;
    private final NoticeBoardRepository noticeBoardRepository;
    private final ReplyService noticeReplyService;

    private final BankBoardService bankBoardService;
    private final BankBoardRepository bankBoardRepository;
    private final ReplyService bankReplyService;

    private final ReportBoardService reportBoardService;
    private final ReportBoardRepository reportBoardRepository;
    private final ReplyService reportReplyService;

    private final S3Service s3Service;
    private final FileService fileService;


    private final BoardControllerService boardControllerService;

    public BoardS3Controller(FreeBoardService freeBoardService,
                             FreeBoardRepository freeBoardRepository, @Qualifier("freeReplyServiceImpl") ReplyService freeReplyService,
                             NoticeBoardService noticeBoardService, NoticeBoardRepository noticeBoardRepository,
                             @Qualifier("noticeReplyServiceImpl") ReplyService noticeReplyService, BankBoardService bankBoardService, BankBoardRepository bankBoardRepository,
                             @Qualifier("bankReplyServiceImpl") ReplyService bankReplyService, ReportBoardService reportBoardService, ReportBoardRepository reportBoardRepository,
                             @Qualifier("reportReplyServiceImpl") ReplyService reportReplyService, S3Service s3Service, FileService fileService, BoardControllerService boardControllerService) {

        this.freeBoardService = freeBoardService;
        this.freeBoardRepository = freeBoardRepository;
        this.freeReplyService = freeReplyService;
        this.noticeBoardService = noticeBoardService;
        this.noticeBoardRepository = noticeBoardRepository;
        this.noticeReplyService = noticeReplyService;
        this.bankBoardService = bankBoardService;
        this.bankBoardRepository = bankBoardRepository;
        this.bankReplyService = bankReplyService;
        this.reportBoardService = reportBoardService;
        this.reportBoardRepository = reportBoardRepository;
        this.reportReplyService = reportReplyService;
        this.s3Service = s3Service;
        this.fileService = fileService;
        this.boardControllerService = boardControllerService;
    }


    @GetMapping("/list") //목록api 페이지 수 넣어서 해당 페이지 게시물 10개씩 데이터 보여주기   http://localhost:8080/boards/list?page=1
    public ResponseEntity<PageResponseDTO<BoardListReplyCountDTO>> FreeList(@RequestParam(defaultValue = "1") int page, PageRequestDTO pageRequestDTO) {

        int size = 11;

        pageRequestDTO.setPage(page);
        pageRequestDTO.setSize(size);

        PageResponseDTO<BoardListReplyCountDTO> responseDTO = freeBoardService.listWithReplyCount(pageRequestDTO);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/search") //게시판 검색 http://localhost:8080/boards/list?page=1&type=w&keyword=user9
    public ResponseEntity<PageResponseDTO<BoardListReplyCountDTO>> FreeSearch(PageRequestDTO pageRequestDTO) {
        PageResponseDTO<BoardListReplyCountDTO> responseDTO = freeBoardService.listWithReplyCount(pageRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/modify/{boardType}/{bno}") // 수정 api -> http://localhost:8080/api/boards/modify/free/1
    public ResponseEntity<String> modifyBoard(@PathVariable String boardType,
                                              @PathVariable Long bno,
                                              @RequestBody @Valid BoardDTO boardDTO,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors.toString());
        }

        BoardType enumBoardType = BoardType.valueOf(boardType);
        boardControllerService.modifyBoard(enumBoardType, bno, boardDTO);

        return ResponseEntity.ok("Board modified successfully.");
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/modify/bank/{boardType}/{bno}") // 수정 api -> http://localhost:8080/api/boards/modify/free/1
    public ResponseEntity<String> modifyBoard(@PathVariable String boardType,
                                              @PathVariable Long bno,
                                              @RequestBody @Valid BankBoardDTO boardDTO,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors.toString());
        }

        boardDTO.setBno(bno);
        bankBoardService.modify(boardDTO);

        return ResponseEntity.ok("Board modified successfully.");
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<Long> registerBoard(@RequestBody BoardDTO boardDTO) {
        BoardType boardType = boardDTO.getBoardType();
        Long bno = boardControllerService.registerBoard(boardType, boardDTO);
        return ResponseEntity.ok(bno);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/register/bank")
    public ResponseEntity<Long> registerBankBoard(@RequestBody BankBoardDTO bankBoardDTO) {
        BoardType boardType = bankBoardDTO.getBoardType();
        Long bno = boardControllerService.registerBankBoard(boardType, bankBoardDTO);
        return ResponseEntity.ok(bno);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/remove/{boardType}/{bno}")
    public ResponseEntity<String> removeBoard(@PathVariable String boardType, @PathVariable Long bno) {
        BoardType enumBoardType = BoardType.valueOf(boardType); // 문자열을 enum으로 변환
        boardControllerService.removeBoard(enumBoardType, bno); // 공통 로직 호출
        return ResponseEntity.ok(enumBoardType + " board removed successfully.");
    }

    @PostMapping(value = "/api/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResponseDTO> uploadFileWithInfo(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("boardType") BoardType boardType,
            @RequestParam("bno") Long bno
    ) {
        try {
            // S3에 파일 업로드 및 정보 저장
            FileResponseDTO fileResponseDto = s3Service.uploadFile(multipartFile);

            // 게시물 번호(bno)로 해당 게시판 객체 생성
            FileEntity fileEntity = null;

            switch (boardType) {
                case FREE:
                    FreeBoard freeBoard = freeBoardService.findById(bno);
                    fileEntity = new FileEntity(
                            fileResponseDto.getFileName(),
                            fileResponseDto.getUuid(),
                            fileResponseDto.getUrl(),
                            boardType,
                            freeBoard,
                            null,
                            null,
                            null
                    );
                    break;
                case NOTICE:
                    NoticeBoard noticeBoard = noticeBoardService.findById(bno);
                    fileEntity = new FileEntity(
                            fileResponseDto.getFileName(),
                            fileResponseDto.getUuid(),
                            fileResponseDto.getUrl(),
                            boardType,
                            null,
                            null,
                            noticeBoard,
                            null
                    );
                    break;
                case REPORT:
                    ReportBoard reportBoard = reportBoardService.findById(bno);
                    fileEntity = new FileEntity(
                            fileResponseDto.getFileName(),
                            fileResponseDto.getUuid(),
                            fileResponseDto.getUrl(),
                            boardType,
                            null,
                            null,
                            null,
                            reportBoard
                    );
                    break;
                case BANK:
                    BankBoard bankBoard = bankBoardService.findById(bno);
                    fileEntity = new FileEntity(
                            fileResponseDto.getFileName(),
                            fileResponseDto.getUuid(),
                            fileResponseDto.getUrl(),
                            boardType,
                            null,
                            bankBoard,
                            null,
                            null
                    );
                    break;
            }

            fileService.save(fileEntity);

            // FileResponseDto에 boardType과 bno 값을 설정하여 반환
            fileResponseDto.setBoardType(boardType);
            fileResponseDto.setBno(bno);

            return ResponseEntity.ok(fileResponseDto);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/api/delete/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable Long id) {
        try {
            FileEntity fileEntity = fileService.getFileById(id);
            if (fileEntity == null) {
                return ResponseEntity.notFound().build();
            }

            // S3에서 파일 삭제
            s3Service.deleteFile(fileEntity.getFileName());

            // 데이터베이스에서 파일 정보 삭제
            fileService.deleteFileById(id);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/api/edit/{id}")
    public ResponseEntity<FileResponseDTO> editFile(@PathVariable Long id, @RequestParam("file") MultipartFile multipartFile,
                                                    @RequestParam("boardType") BoardType boardType,
                                                    @RequestParam("bno") Long bno) {
        try {
            FileEntity fileEntity = fileService.getFileById(id);
            if (fileEntity == null) {
                return ResponseEntity.notFound().build();
            }

            // 기존 파일을 S3에서 삭제
            s3Service.deleteFile(fileEntity.getFileName());

            // 새로운 파일 업로드
            FileResponseDTO newFileResponseDto = s3Service.uploadFile(multipartFile);

            fileEntity.setFileName(newFileResponseDto.getFileName());
            fileEntity.setS3Url(newFileResponseDto.getUrl());

            newFileResponseDto.setBoardType(boardType);
            newFileResponseDto.setBno(bno);

            fileService.save(fileEntity);

            return ResponseEntity.ok(newFileResponseDto);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
