package com.example.capstone_1.controller;

import com.example.capstone_1.domain.*;
import com.example.capstone_1.dto.*;
import com.example.capstone_1.repository.*;
import com.example.capstone_1.service.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

    private final ReportBoardService reportBoardService;
    private final ReportBoardRepository reportBoardRepository;
    private final ReplyService reportReplyService;

    private final S3Service s3Service;
    private final FileService fileService;


    private final BoardControllerService boardControllerService;

    private final MemberService memberService;
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
                             @Qualifier("reportReplyServiceImpl") ReplyService reportReplyService, S3Service s3Service, FileService fileService, BoardControllerService boardControllerService, MemberService memberService) {

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
        this.memberService = memberService;
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


//    게시판 내용 및 이미지 조회 api
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
        Long secret = boardRepository.getSecretOfBoard(bno);

        // 현재 사용자가 로그인한 경우에만 가져오도록
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = null;
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            currentUser = (User) authentication.getPrincipal();
        }
        if (currentUser==null){  //로그인 안한 경우
            if (0L == secret){
                BoardType enumBoardType = BoardType.valueOf(boardType); // 문자열 enum으로
                Board_File_DTO boardWithImages = boardControllerService.getBoardWithImages(enumBoardType, bno);
                return ResponseEntity.ok(boardWithImages);
            }else{
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to read this board.");
            }

        }else{

            if(1L == secret && currentUser.getUsername().equals(writer) ||  currentUser.getUsername().equals("darkest0722@gmail.com")){
                BoardType enumBoardType = BoardType.valueOf(boardType); // 문자열 enum으로
                Board_File_DTO boardWithImages = boardControllerService.getBoardWithImages(enumBoardType, bno);
                return ResponseEntity.ok(boardWithImages);
            }else{
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to read this board.");
            }

        }

    }



//    @GetMapping("/{boardType}/{bno}/withImages")
//    public ResponseEntity<?> getBoardWithImages(
//            @PathVariable String boardType,
//            @PathVariable Long bno,
//            HttpServletRequest request,
//            HttpServletResponse response) {
//        // 클라이언트에서 쿠키를 읽어옴
//        Cookie[] cookies = request.getCookies();
//        boolean isAlreadyRead = false;
//
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("read_board_" + bno)) {
//                    // 쿠키에 해당 게시물을 이미 읽었다는 정보가 있으면 중복 읽기로 판단
//                    isAlreadyRead = true;
//                    break;
//                }
//            }
//        }
//
//        if (!isAlreadyRead) {
//            // 조회수 업데이트
//            freeBoardService.updateViewCount(bno);
//
//            // 클라이언트에게 읽었다는 정보를 담은 쿠키를 전송
//            Cookie cookie = new Cookie("read_board_" + bno, "true");
//            cookie.setMaxAge(60 * 60 * 24); // 24시간 동안 유지되는 쿠키
//            response.addCookie(cookie);
//        }
//
//        BoardRepository boardRepository = null;
//        if ("FREE".equals(boardType)) {
//            boardRepository = freerepository;
//        } else if ("NOTICE".equals(boardType)) {
//            boardRepository = noticerepository;
//        } else if ("REPORT".equals(boardType)) {
//            boardRepository = reportrepository;
//        }
//        String writer = boardRepository.getWriterOfBoard(bno);
//        Long secret = boardRepository.getSecretOfBoard(bno);
//
//        // 현재 사용자가 로그인한 경우에만 가져오도록
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User currentUser = null;
//        if (authentication != null && authentication.getPrincipal() instanceof User) {
//            currentUser = (User) authentication.getPrincipal();
//        }
//        if (currentUser == null) {  //로그인 안한 경우
//            if (0L == secret) {
//                BoardType enumBoardType = BoardType.valueOf(boardType); // 문자열 enum으로
//                Board_File_DTO boardWithImages = boardControllerService.getBoardWithImages(enumBoardType, bno);
//                return ResponseEntity.ok(boardWithImages);
//            } else {
//
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to read this board.");
//            }
//        } else {
//            if (1L == secret && currentUser.getUsername().equals(writer) || currentUser.getUsername().equals("darkest0722@gmail.com")) {
//                BoardType enumBoardType = BoardType.valueOf(boardType); // 문자열 enum으로
//                Board_File_DTO boardWithImages = boardControllerService.getBoardWithImages(enumBoardType, bno);
//                return ResponseEntity.ok(boardWithImages);
//            } else {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to read this board.");
//            }
//        }
//    }









    //with images

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
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<?> registerBoard(@RequestBody BoardDTO boardDTO) {
        BoardType boardType = boardDTO.getBoardType();

        // BoardType이 NOTICE이고 현재 사용자가 관리자가 아니면 권한 없음 응답
        if (boardType == BoardType.NOTICE && !SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to register on NOTICE board.");
        }


        Long bno = boardControllerService.registerBoard(boardType, boardDTO);
        return ResponseEntity.ok(bno);
    }

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
            fileResponseDto.setId(fileEntity.getId());

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
            fileEntity.setUuid(newFileResponseDto.getUuid());

            newFileResponseDto.setBoardType(boardType);
            newFileResponseDto.setBno(bno);
            newFileResponseDto.setId(id);


            fileService.save(fileEntity);

            return ResponseEntity.ok(newFileResponseDto);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
