package com.example.capstone_1.controller;

import com.example.capstone_1.dto.*;
import com.example.capstone_1.service.*;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Log4j2
@RequestMapping("/boards")
public class ProdBoardController_front {

    private final S3Uploader s3Uploader;

    private final FreeBoardService freeBoardService;
    private final ReplyService freeReplyService;
    private final ReportBoardService reportBoardService;
    private final ReplyService reportReplyService;
    private final NoticeBoardService noticeBoardService;
    private final ReplyService noticeReplyService;
    private final BankBoardService bankBoardService;
    private final ReplyService bankReplyService;


    public ProdBoardController_front(FreeBoardService freeBoardService,
                                     @Qualifier("freeReplyServiceImpl") ReplyService freeReplyService,
                                     ReportBoardService reportBoardService,
                                     @Qualifier("reportReplyServiceImpl") ReplyService reportReplyService,
                                     NoticeBoardService noticeBoardService,
                                     @Qualifier("noticeReplyServiceImpl") ReplyService noticeReplyService,
                                     BankBoardService bankBoardService,
                                     @Qualifier("bankReplyServiceImpl") ReplyService bankReplyService,
                                     S3Uploader s3Uploader) {
        this.freeBoardService = freeBoardService;
        this.freeReplyService = freeReplyService;
        this.reportBoardService = reportBoardService;
        this.reportReplyService = reportReplyService;
        this.noticeBoardService = noticeBoardService;
        this.noticeReplyService = noticeReplyService;
        this.bankBoardService = bankBoardService;
        this.bankReplyService = bankReplyService;
        this.s3Uploader = s3Uploader; // s3Uploader 초기화
    }

    @GetMapping("/free/list") //페이지 수 넣어서 해당 페이지 게시물 10개씩 데이터 보여주기   http://localhost:8080/api/boards/list?page=1
    public ResponseEntity<PageResponseDTO<BoardListAllDTO>> FreeList(@RequestParam(defaultValue = "1") int page, PageRequestDTO pageRequestDTO) {

        int size = 11;

        pageRequestDTO.setPage(page);
        pageRequestDTO.setSize(size);

        PageResponseDTO<BoardListAllDTO> responseDTO = freeBoardService.listWithAll(pageRequestDTO);

        return ResponseEntity.ok(responseDTO);
    }

//    @GetMapping("/report/list")
//    public ResponseEntity<PageResponseDTO<BoardListAllDTO>> ReprotList(@RequestParam(defaultValue = "1") int page, PageRequestDTO pageRequestDTO) {
//
//        int size = 11;
//
//        pageRequestDTO.setPage(page);
//        pageRequestDTO.setSize(size);
//
//        PageResponseDTO<BoardListAllDTO> responseDTO = reportBoardService.listWithAll(pageRequestDTO);
//
//        return ResponseEntity.ok(responseDTO);
//    }
//
//    @GetMapping("/notice/list")
//    public ResponseEntity<PageResponseDTO<BoardListAllDTO>> NoticeList(@RequestParam(defaultValue = "1") int page, PageRequestDTO pageRequestDTO) {
//
//        int size = 11;
//
//        pageRequestDTO.setPage(page);
//        pageRequestDTO.setSize(size);
//
//        PageResponseDTO<BoardListAllDTO> responseDTO = reportBoardService.listWithAll(pageRequestDTO);
//
//        return ResponseEntity.ok(responseDTO);
//    }
//
//    @GetMapping("/bank/list")
//    public ResponseEntity<PageResponseDTO<BoardListAllDTO>> BankList(@RequestParam(defaultValue = "1") int page, PageRequestDTO pageRequestDTO) {
//
//        int size = 11;
//
//        pageRequestDTO.setPage(page);
//        pageRequestDTO.setSize(size);
//
//        PageResponseDTO<BoardListAllDTO> responseDTO = reportBoardService.listWithAll(pageRequestDTO);
//
//        return ResponseEntity.ok(responseDTO);
//    }

    @GetMapping("/free/search") //게시판 검색 http://localhost:8080/api/boards/list?page=1&type=w&keyword=user9
    public ResponseEntity<PageResponseDTO<BoardListAllDTO>> FreeSearch(PageRequestDTO pageRequestDTO) {
        PageResponseDTO<BoardListAllDTO> responseDTO = freeBoardService.listWithAll(pageRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

//    @GetMapping("/report/search")
//    public ResponseEntity<PageResponseDTO<BoardListAllDTO>> ReportSearch(PageRequestDTO pageRequestDTO) {
//        PageResponseDTO<BoardListAllDTO> responseDTO = reportBoardService.listWithAll(pageRequestDTO);
//        return ResponseEntity.ok(responseDTO);
//    }
//
//    @GetMapping("/notice/search")
//    public ResponseEntity<PageResponseDTO<BoardListAllDTO>> NoticeSearch(PageRequestDTO pageRequestDTO) {
//        PageResponseDTO<BoardListAllDTO> responseDTO = noticeBoardService.listWithAll(pageRequestDTO);
//        return ResponseEntity.ok(responseDTO);
//    }
//    @GetMapping("/bank/search")
//    public ResponseEntity<PageResponseDTO<BoardListAllDTO>> BankSearch(PageRequestDTO pageRequestDTO) {
//        PageResponseDTO<BoardListAllDTO> responseDTO = bankBoardService.listWithAll(pageRequestDTO);
//        return ResponseEntity.ok(responseDTO);
//    }


    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/free/register") //게시판 등록 http://localhost:8080/api/boards/register
    public ResponseEntity<Long> FreeRegisterPost(@Valid @RequestBody BoardDTO boardDTO, BindingResult bindingResult,
                                                 @RequestParam(value = "files", required = false) List<MultipartFile> files ) {


        if (bindingResult.hasErrors()) {
            // 유효성 검사 실패 시 오류 응답
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                String fieldName = fieldError.getField();
                String errorMessage = fieldError.getDefaultMessage();
                // 에러가 발생한 필드와 에러 메시지를 로깅하거나 사용자에게 알림
                System.out.println("에러 발생 필드: " + fieldName);
                System.out.println("에러 메시지: " + errorMessage);
            }

            return ResponseEntity.badRequest().build();
        }
        List<String> imageUrls = new ArrayList<>();
        if (files != null) {
            for (MultipartFile file : files) {
                try {
                    String imageUrl = s3Uploader.upload(file, UUID.randomUUID().toString());
                    imageUrls.add(imageUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("에러 메시지", e);
                }
            }
        }
        boardDTO.setFileNames(imageUrls);


        Long bno = freeBoardService.register(boardDTO);
        return ResponseEntity.ok(bno);
    }

//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
//    @PostMapping("/report/register")
//    public ResponseEntity<Long> ReportRegisterPost(@Valid @RequestBody BoardDTO boardDTO, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        Long bno = reportBoardService.register(boardDTO);
//        return ResponseEntity.ok(bno);
//    }
//
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PostMapping("/notice/register")
//    public ResponseEntity<Long> NoticeRegisterPost(@Valid @RequestBody BoardDTO boardDTO, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        Long bno = noticeBoardService.register(boardDTO);
//        return ResponseEntity.ok(bno);
//    }
//
//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
//    @PostMapping("/bank/register")
//    public ResponseEntity<Long> BankRegisterPost(@Valid @RequestBody BankBoardDTO boardDTO, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        Long bno = bankBoardService.register(boardDTO);
//        return ResponseEntity.ok(bno);
//    }

//    {
//        "title": "Updated Title",
//            "content": "Updated Content",
//            "writer": "Updated Writer"
//
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
//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
//    @PutMapping("/report/modify/{bno}")
//    public ResponseEntity<String> ReportModify(@PathVariable Long bno,
//                                         @RequestBody @Valid BoardDTO boardDTO,
//                                         BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            List<String> errors = bindingResult.getAllErrors().stream()
//                    .map(ObjectError::getDefaultMessage)
//                    .collect(Collectors.toList());
//            return ResponseEntity.badRequest().body(errors.toString());
//        }
//
//        boardDTO.setBno(bno);
//        freeBoardService.modify(boardDTO);
//
//        return ResponseEntity.ok("Board modified successfully.");
//    }
//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
//    @PutMapping("/notice/modify/{bno}")
//    public ResponseEntity<String> NoticeModify(@PathVariable Long bno,
//                                               @RequestBody @Valid BoardDTO boardDTO,
//                                               BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            List<String> errors = bindingResult.getAllErrors().stream()
//                    .map(ObjectError::getDefaultMessage)
//                    .collect(Collectors.toList());
//            return ResponseEntity.badRequest().body(errors.toString());
//        }
//
//        boardDTO.setBno(bno);
//        noticeBoardService.modify(boardDTO);
//
//        return ResponseEntity.ok("Board modified successfully.");
//    }
//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
//    @PutMapping("/bank/modify/{bno}")
//    public ResponseEntity<String> BankModify(@PathVariable Long bno,
//                                               @RequestBody @Valid BankBoardDTO boardDTO,
//                                               BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            List<String> errors = bindingResult.getAllErrors().stream()
//                    .map(ObjectError::getDefaultMessage)
//                    .collect(Collectors.toList());
//            return ResponseEntity.badRequest().body(errors.toString());
//        }
//
//        boardDTO.setBno(bno);
//        bankBoardService.modify(boardDTO);
//
//        return ResponseEntity.ok("Board modified successfully.");
//    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/free/remove/{bno}") //게시판 삭제 -> http://localhost:8080/api/boards/remove/1
    public ResponseEntity<String> FreeRemoveBoard(@PathVariable Long bno) {

        // 파일 삭제
        BoardDTO boardDTO = freeBoardService.readOne(bno);
        if (boardDTO.getFileNames() != null && !boardDTO.getFileNames().isEmpty()) {
            removeFiles(boardDTO.getFileNames());
        }

        //게시물에 있는 댓글들 삭제하려면, 해당 게시판의 댓글 번호들을 알아야 겠지.
        freeReplyService.removeRepliesByBoardId(bno);

        freeBoardService.remove(bno); //게시물 삭제

        return ResponseEntity.ok("Board removed successfully.");
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/report/remove/{bno}") //게시판 삭제 -> http://localhost:8080/api/boards/remove/1
    public ResponseEntity<String> ReportRemoveBoard(@PathVariable Long bno) {

        // 파일 삭제
        BoardDTO boardDTO = reportBoardService.readOne(bno);
        if (boardDTO.getFileNames() != null && !boardDTO.getFileNames().isEmpty()) {
            removeFiles(boardDTO.getFileNames());
        }

        //게시물에 있는 댓글들 삭제하려면, 해당 게시판의 댓글 번호들을 알아야 겠지.
        reportReplyService.removeRepliesByBoardId(bno);

        reportBoardService.remove(bno); //게시물 삭제

        return ResponseEntity.ok("Board removed successfully.");
    }
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @DeleteMapping("/notice/remove/{bno}") //게시판 삭제 -> http://localhost:8080/api/boards/remove/1
//    public ResponseEntity<String> NoticeRemoveBoard(@PathVariable Long bno) {
//
//        // 파일 삭제
//        BoardDTO boardDTO = noticeBoardService.readOne(bno);
//        if (boardDTO.getFileNames() != null && !boardDTO.getFileNames().isEmpty()) {
//            removeFiles(boardDTO.getFileNames());
//        }
//
//        //게시물에 있는 댓글들 삭제하려면, 해당 게시판의 댓글 번호들을 알아야 겠지.
//        noticeReplyService.removeRepliesByBoardId(bno);
//
//        noticeBoardService.remove(bno); //게시물 삭제
//
//        return ResponseEntity.ok("Board removed successfully.");
//    }
//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
//    @DeleteMapping("/bank/remove/{bno}") //게시판 삭제 -> http://localhost:8080/api/boards/remove/1
//    public ResponseEntity<String> BankRemoveBoard(@PathVariable Long bno) {
//
//        // 파일 삭제
//        BoardDTO boardDTO = bankBoardService.readOne(bno);
//        if (boardDTO.getFileNames() != null && !boardDTO.getFileNames().isEmpty()) {
//            removeFiles(boardDTO.getFileNames());
//        }
//
//        //게시물에 있는 댓글들 삭제하려면, 해당 게시판의 댓글 번호들을 알아야 겠지.
//        bankReplyService.removeRepliesByBoardId(bno);
//
//        bankBoardService.remove(bno); //게시물 삭제
//
//        return ResponseEntity.ok("Board removed successfully.");
//    }

    public void removeFiles(List<String> files) {
        for (String fileName : files) {
            try {
                // AWS S3에서 파일 삭제
                s3Uploader.deleteFile(fileName);

                // 섬네일 파일 삭제
                if (fileName.startsWith("images/")) { // 이미지 파일에 대한 섬네일도 삭제
                    String thumbnailFileName = "thumbnails/s_" + fileName.substring(7);
                    s3Uploader.deleteFile(thumbnailFileName);
                }
            } catch (Exception e) {
                log.error("Error deleting file from AWS S3: " + e.getMessage());
            }
        }
    }
}


