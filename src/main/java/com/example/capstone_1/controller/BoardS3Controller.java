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
import org.springframework.validation.FieldError;
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


//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
//    @PostMapping("/free/register") //게시판 등록 http://localhost:8080/api/boards/register
//    public ResponseEntity<Long> FreeRegisterPost(@Valid @RequestBody BoardDTO boardDTO, BindingResult bindingResult) {
//
//
//        if (bindingResult.hasErrors()) {
//            // 유효성 검사 실패 시 오류 응답
//            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//            for (FieldError fieldError : fieldErrors) {
//                String fieldName = fieldError.getField();
//                String errorMessage = fieldError.getDefaultMessage();
//                // 에러가 발생한 필드와 에러 메시지를 로깅하거나 사용자에게 알림
//                System.out.println("에러 발생 필드: " + fieldName);
//                System.out.println("에러 메시지: " + errorMessage);
//            }
//            return ResponseEntity.badRequest().build();
//        }
//
//        //boardDTO.setBoardType(BoardType.FREE);
//        Long bno = freeBoardService.register(boardDTO);
//
//        return ResponseEntity.ok(bno);
//    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/free/modify/{bno}") //수정 api -> http://localhost:8080/api/boards/modify/1
    public ResponseEntity<String> FreeModify(@PathVariable Long bno,
                                             @RequestBody @Valid BoardDTO boardDTO,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors.toString());
        }

        boardDTO.setBno(bno);
        freeBoardService.modify(boardDTO);

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




//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
//    @DeleteMapping("/free/remove/{bno}") //게시판 삭제 -> http://localhost:8080/api/boards/remove/1
//    public ResponseEntity<String> FreeRemoveBoard(@PathVariable Long bno) {
//
//        // 해당 게시물 정보를 가져옴
//        FreeBoard freeBoard = freeBoardRepository.findById(bno)
//                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));
//
//        // 연결된 이미지 파일들도 함께 삭제
//        List<FileEntity> imageFiles = freeBoard.getFiles();
//        if (imageFiles != null) {
//            for (FileEntity fileEntity : imageFiles) {
//                s3Service.deleteFile(fileEntity.getFileName());
//                fileService.deleteFileById(fileEntity.getId());
//            }
//        }
//
//        //게시물에 있는 댓글들 삭제하려면, 해당 게시판의 댓글 번호들을 알아야 겠지.
//        freeReplyService.removeRepliesByBoardId(bno);
//
//        freeBoardService.remove(bno); //게시물 삭제
//
//        return ResponseEntity.ok("Board removed successfully.");
//    }

    //이건 게시판 url패턴으로 나누어서 각각 컨트롤러 구현해서 삭제해야함

    //아래는 게시판 종류와 게시판 번호를 줘서 여러개의 게시판을 하나의 컨트롤러로 함.

//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
//    @DeleteMapping("/remove/{boardType}/{bno}") // 게시물 삭제 -> http://localhost:8080/api/boards/free/1
//    public ResponseEntity<String> removeBoard(@PathVariable String boardType, @PathVariable Long bno) {
//        if ("FREE".equals(boardType)) {
//
//            FreeBoard freeBoard = freeBoardRepository.findById(bno)
//                    .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));
//
//            // 연결된 이미지 파일들도 함께 삭제
//            List<FileEntity> imageFiles = freeBoard.getFiles();
//            if (imageFiles != null) {
//                for (FileEntity fileEntity : imageFiles) {
//                    s3Service.deleteFile(fileEntity.getFileName());
//                    fileService.deleteFileById(fileEntity.getId());
//                }
//            }
//
//            freeReplyService.removeRepliesByBoardId(bno);  //게시물에 있는 댓글들 삭제하려면, 해당 게시판의 댓글 번호들을 알아야 겠지.
//            freeBoardService.remove(bno);  //게시물 삭제
//            return ResponseEntity.ok("Free board removed successfully.");
//
//        } else if ("NOTICE".equals(boardType)) {
//
//            NoticeBoard noticeBoard = noticeBoardRepository.findById(bno)
//                    .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));
//
//            List<FileEntity> imageFiles = noticeBoard.getFiles();
//            if (imageFiles != null) {
//                for (FileEntity fileEntity : imageFiles) {
//                    s3Service.deleteFile(fileEntity.getFileName());
//                    fileService.deleteFileById(fileEntity.getId());
//                }
//            }
//
//            noticeReplyService.removeRepliesByBoardId(bno);
//            noticeBoardService.remove(bno);
//            return ResponseEntity.ok("Notice board removed successfully.");
//
//
//        }else if ("REPORT".equals(boardType)) {
//
//            ReportBoard reportBoard = reportBoardRepository.findById(bno)
//                    .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));
//
//            List<FileEntity> imageFiles = reportBoard.getFiles();
//            if (imageFiles != null) {
//                for (FileEntity fileEntity : imageFiles) {
//                    s3Service.deleteFile(fileEntity.getFileName());
//                    fileService.deleteFileById(fileEntity.getId());
//                }
//            }
//
//            reportReplyService.removeRepliesByBoardId(bno);
//            reportBoardService.remove(bno);
//            return ResponseEntity.ok("Report board removed successfully.");
//
//        }else if ("BANK".equals(boardType)) {
//
//            BankBoard bankBoard = bankBoardRepository.findById(bno)
//                    .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));
//
//            List<FileEntity> imageFiles = bankBoard.getFiles();
//            if (imageFiles != null) {
//                for (FileEntity fileEntity : imageFiles) {
//                    s3Service.deleteFile(fileEntity.getFileName());
//                    fileService.deleteFileById(fileEntity.getId());
//                }
//            }
//
//            bankReplyService.removeRepliesByBoardId(bno);
//            bankBoardService.remove(bno);
//            return ResponseEntity.ok("Bank board removed successfully.");
//
//        }
//
//        return ResponseEntity.badRequest().body("Invalid board type: " + boardType);
//    }












    @PostMapping(value="/api/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResponseDTO> uploadFileWithInfo(@RequestParam("file") MultipartFile multipartFile,
                                                              @RequestParam("boardType") BoardType boardType,
                                                              @RequestParam("bno") Long bno) {
        try {
            // S3에 파일 업로드 및 정보 저장
            FileResponseDTO fileResponseDto = s3Service.uploadFile(multipartFile);

            // 게시물 번호(bno)로 FreeBoard 객체 생성
            FreeBoard freeBoard = freeBoardService.findById(bno);

            // FileResponseDto에서 필요한 정보로 FileEntity 생성
            FileEntity fileEntity = new FileEntity(
                    fileResponseDto.getFileName(),
                    fileResponseDto.getUuid(),
                    fileResponseDto.getUrl(),
                    boardType,
                    freeBoard,
                    null,
                    null,
                    null
            );

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

//    @GetMapping("/api/files") //업로드한 파일 정보 프론트에게 json형태로 주는 컨트롤러
//    public ResponseEntity<List<FileEntity>> getFiles() {
//        List<FileEntity> fileList = fileService.getFiles();
//        return ResponseEntity.ok(fileList);
//    }

}
