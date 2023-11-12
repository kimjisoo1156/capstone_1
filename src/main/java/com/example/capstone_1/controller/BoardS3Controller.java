package com.example.capstone_1.controller;

import com.example.capstone_1.domain.*;
import com.example.capstone_1.dto.*;
import com.example.capstone_1.repository.*;
import com.example.capstone_1.service.*;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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

//    private final BankBoardService bankBoardService;
//    private final BankBoardRepository bankBoardRepository;
//    private final ReplyService bankReplyService;

    private final ReportBoardService reportBoardService;
    private final ReportBoardRepository reportBoardRepository;
    private final ReplyService reportReplyService;

    private final S3Service s3Service;
    private final FileService fileService;


    private final BoardControllerService boardControllerService;

    private final UserService userService;
    @Autowired
    @Qualifier("freeBoardServiceImpl")
    private BoardRepository freerepository;

    @Autowired
    @Qualifier("noticeBoardServiceImpl")
    private BoardRepository noticerepository;

    @Autowired
    @Qualifier("reportBoardServiceImpl")
    private BoardRepository reportrepository;

    public BoardS3Controller(FreeBoardService freeBoardService,
                             FreeBoardRepository freeBoardRepository, @Qualifier("freeReplyServiceImpl") ReplyService freeReplyService,
                             NoticeBoardService noticeBoardService, NoticeBoardRepository noticeBoardRepository,
                             @Qualifier("noticeReplyServiceImpl") ReplyService noticeReplyService,
                             ReportBoardService reportBoardService, ReportBoardRepository reportBoardRepository,
                             @Qualifier("reportReplyServiceImpl") ReplyService reportReplyService, S3Service s3Service, FileService fileService, BoardControllerService boardControllerService, UserService userService) {

        this.freeBoardService = freeBoardService;
        this.freeBoardRepository = freeBoardRepository;
        this.freeReplyService = freeReplyService;
        this.noticeBoardService = noticeBoardService;
        this.noticeBoardRepository = noticeBoardRepository;
        this.noticeReplyService = noticeReplyService;
        this.reportBoardService = reportBoardService;
        this.reportBoardRepository = reportBoardRepository;
        this.reportReplyService = reportReplyService;
        this.s3Service = s3Service;
        this.fileService = fileService;
        this.boardControllerService = boardControllerService;
        this.userService = userService;
    }


//    @GetMapping("/list") //목록api 페이지 수 넣어서 해당 페이지 게시물 10개씩 데이터 보여주기   http://localhost:8080/boards/list?page=1

    @GetMapping("/{boardType}/list")
    public ResponseEntity<PageResponseDTO<BoardListReplyCountDTO>> listBoards(
            @PathVariable String boardType,
            @RequestParam(defaultValue = "1") int page,
            PageRequestDTO pageRequestDTO) {

        int size = 10;

        pageRequestDTO.setPage(page);
        pageRequestDTO.setSize(size);

        PageResponseDTO<BoardListReplyCountDTO> responseDTO = boardControllerService.listWithReplyCount(boardType, pageRequestDTO);

        return ResponseEntity.ok(responseDTO);
    }
    @GetMapping("/{boardType}/search")
    public ResponseEntity<PageResponseDTO<BoardListReplyCountDTO>> searchBoards(
            @PathVariable String boardType,
            PageRequestDTO pageRequestDTO) {

        PageResponseDTO<BoardListReplyCountDTO> responseDTO = boardControllerService.searchBoards(boardType, pageRequestDTO);

        return ResponseEntity.ok(responseDTO);
    }


    //게시판 내용 및 이미지 조회 api
    @GetMapping("/{boardType}/{bno}/withImages")
    public ResponseEntity<?> getBoardWithImages(
            @PathVariable String boardType,
            @PathVariable Long bno) {

        BoardRepository boardRepository = null;
        if ("FREE".equals(boardType)) {
            boardRepository = freerepository;
        } else if ("NOTICE".equals(boardType)) {
            boardRepository = noticerepository;
        } else if ("REPORT".equals(boardType)) {
            boardRepository = reportrepository;
        }


        String writer = boardRepository.getWriterOfBoard(bno);
        String secret = boardRepository.getSecretOfBoard(bno);
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if("1".equals(secret) && !currentUser.getUsername().equals(writer) && !currentUser.getUsername().equals("darkest0722@gmail.com")){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to read this board.");
        }else{
            BoardType enumBoardType = BoardType.valueOf(boardType); //문자열 enum으로
            Board_File_DTO boardWithImages = boardControllerService.getBoardWithImages(enumBoardType, bno);
            return ResponseEntity.ok(boardWithImages);
        }

    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/modify/{boardType}/{bno}") // 수정 api
    public ResponseEntity<String> modifyBoard(@PathVariable String boardType,
                                              @PathVariable Long bno,
                                              @RequestBody @Valid BoardDTO boardDTO,
                                              BindingResult bindingResult) {
        BoardRepository boardRepository = null;
        if ("FREE".equals(boardType)) {
            boardRepository = freerepository;
        } else if ("NOTICE".equals(boardType)) {
            boardRepository = noticerepository;
        } else if ("REPORT".equals(boardType)) {
            boardRepository = reportrepository;
        }
        String writer = boardRepository.getWriterOfBoard(bno);
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser.getUsername().equals(writer)||currentUser.getUsername().equals("darkest0722@gmail.com")){
            if (bindingResult.hasErrors()) {
                List<String> errors = bindingResult.getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors.toString());
            }

            BoardType enumBoardType = BoardType.valueOf(boardType);
            boardControllerService.modifyBoard(enumBoardType, bno, boardDTO);

            return ResponseEntity.ok("Board modified successfully.");
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to delete this board.");
        }
    }
//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
//    @PutMapping("/modify/bank/{boardType}/{bno}") // 수정 api -> http://localhost:8080/api/boards/modify/free/1
//    public ResponseEntity<String> modifyBoard(@PathVariable String boardType,
//                                              @PathVariable Long bno,
//                                              @RequestBody @Valid BankBoardDTO boardDTO,
//                                              BindingResult bindingResult) {
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
    @PostMapping("/register")
    public ResponseEntity<Long> registerBoard(@RequestBody BoardDTO boardDTO) {
        BoardType boardType = boardDTO.getBoardType();
        Long bno = boardControllerService.registerBoard(boardType, boardDTO);
        return ResponseEntity.ok(bno);
    }

//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
//    @PostMapping("/register/bank")
//    public ResponseEntity<Long> registerBankBoard(@RequestBody BankBoardDTO bankBoardDTO) {
//        BoardType boardType = bankBoardDTO.getBoardType();
//        Long bno = boardControllerService.registerBankBoard(boardType, bankBoardDTO);
//        return ResponseEntity.ok(bno);
//    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/remove/{boardType}/{bno}")
    public ResponseEntity<String> removeBoard(@PathVariable String boardType, @PathVariable Long bno) {

        BoardRepository boardRepository = null;
        if ("FREE".equals(boardType)) {
            boardRepository = freerepository;
        } else if ("NOTICE".equals(boardType)) {
            boardRepository = noticerepository;
        } else if ("REPORT".equals(boardType)) {
            boardRepository = reportrepository;
        }
        String writer = boardRepository.getWriterOfBoard(bno);
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser.getUsername().equals(writer)||currentUser.getUsername().equals("darkest0722@gmail.com")) {

            BoardType enumBoardType = BoardType.valueOf(boardType); // 문자열을 enum으로 변환
            boardControllerService.removeBoard(enumBoardType, bno); // 공통 로직 호출
            return ResponseEntity.ok(enumBoardType + " board removed successfully.");

        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to delete this board.");
        }

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
                            reportBoard
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
