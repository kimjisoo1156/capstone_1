package com.example.capstone_1.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.example.capstone_1.dto.*;
import com.example.capstone_1.service.FreeBoardService;
import com.example.capstone_1.service.ReplyService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Log4j2
@RequestMapping("/boards")
public class BoardController_front {
    private final FreeBoardService freeBoardService;
    private final ReplyService freeReplyService;

    @Autowired
    private AmazonS3 amazonS3;

    public BoardController_front(FreeBoardService freeBoardService,
                                 @Qualifier("freeReplyServiceImpl") ReplyService freeReplyService) {
        this.freeBoardService = freeBoardService;
        this.freeReplyService = freeReplyService;
    }
    @GetMapping("/free/list") //페이지 수 넣어서 해당 페이지 게시물 10개씩 데이터 보여주기   http://localhost:8080/api/boards/list?page=1
    public ResponseEntity<PageResponseDTO<BoardListAllDTO>> FreeList(@RequestParam(defaultValue = "1") int page, PageRequestDTO pageRequestDTO) {

        int size = 11;

        pageRequestDTO.setPage(page);
        pageRequestDTO.setSize(size);

        PageResponseDTO<BoardListAllDTO> responseDTO = freeBoardService.listWithAll(pageRequestDTO);

        return ResponseEntity.ok(responseDTO);
    }
    @GetMapping("/free/search") //게시판 검색 http://localhost:8080/api/boards/list?page=1&type=w&keyword=user9
    public ResponseEntity<PageResponseDTO<BoardListAllDTO>> FreeSearch(PageRequestDTO pageRequestDTO) {
        PageResponseDTO<BoardListAllDTO> responseDTO = freeBoardService.listWithAll(pageRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/free/register") //게시판 등록 http://localhost:8080/api/boards/register
    public ResponseEntity<Long> FreeRegisterPost(@Valid @RequestBody BoardDTO boardDTO, BindingResult bindingResult) {


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

        Long bno = freeBoardService.register(boardDTO);
        return ResponseEntity.ok(bno);
    }
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

}
