package com.example.capstone_1.controller;

import com.example.capstone_1.dto.PageRequestDTO;
import com.example.capstone_1.dto.PageResponseDTO;
import com.example.capstone_1.dto.ReplyDTO;
import com.example.capstone_1.repository.ReplyRepository;
import com.example.capstone_1.service.CommentService;
import com.example.capstone_1.service.ReplyService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/replies")
@Log4j2

public class ReplyController {

    private final ReplyRepository freereplyrepository;
    private final ReplyRepository reportreplyrepository;
    private final ReplyRepository noticereplyrepository;

    private final ReplyService freeReplyService;

    private final CommentService commentService;
    public ReplyController(@Qualifier("freeReplyServiceImpl")ReplyRepository freereplyrepository,
                           @Qualifier("reportReplyServiceImpl") ReplyRepository reportreplyrepository,
                           @Qualifier("noticeReplyServiceImpl")ReplyRepository noticereplyrepository,
                           @Qualifier("freeReplyServiceImpl") ReplyService freeReplyService, CommentService commentService) {
        this.freereplyrepository = freereplyrepository;
        this.reportreplyrepository = reportreplyrepository;
        this.noticereplyrepository = noticereplyrepository;
        this.freeReplyService = freeReplyService;
        this.commentService = commentService;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")

    @PostMapping("/{boardType}/register")
    public ResponseEntity<Map<String, Long>> registerComment(
            @PathVariable String boardType,
            @Valid @RequestBody ReplyDTO commentDTO,
            BindingResult bindingResult) throws  BindException {

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        Long rno = commentService.register(boardType, commentDTO);

        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno", rno);

        return ResponseEntity.ok(resultMap);
    }

    @GetMapping("/{boardType}/list/{bno}")
    public ResponseEntity<PageResponseDTO<ReplyDTO>> getListOfBoard(
            @PathVariable String boardType,
            @PathVariable Long bno,
            PageRequestDTO pageRequestDTO) {

        PageResponseDTO<ReplyDTO> responseDTO = commentService.getListOfBoard(boardType, bno, pageRequestDTO);

        return ResponseEntity.ok(responseDTO);
    }
    @GetMapping("/{boardType}/{rno}")
    public ResponseEntity<ReplyDTO> getComment(
            @PathVariable String boardType,
            @PathVariable Long rno) {

        ReplyDTO replyDTO = commentService.read(boardType, rno);
        return ResponseEntity.ok(replyDTO);
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{boardType}/{rno}")
    public ResponseEntity<?> removeComment(
            @PathVariable String boardType,
            @PathVariable Long rno) {

        ReplyRepository replyRepository = null;

        if ("FREE".equals(boardType)) {
            replyRepository = freereplyrepository;
        } else if ("NOTICE".equals(boardType)) {
            replyRepository = noticereplyrepository;
        } else if ("REPORT".equals(boardType)) {
            replyRepository = reportreplyrepository;
        }
        String writer = replyRepository.getWriterOfReply(rno);
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser.getUsername().equals(writer)||currentUser.getUsername().equals("darkest0722@gmail.com")) {
            commentService.remove(boardType, rno);

            Map<String, Long> resultMap = new HashMap<>();
            resultMap.put("rno", rno);

            return ResponseEntity.ok(resultMap);
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to delete this replies");
        }

    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/{boardType}/{rno}")
    public ResponseEntity<?> modifyComment(
            @PathVariable String boardType,
            @PathVariable Long rno,
            @RequestBody ReplyDTO commentDTO) {

        ReplyRepository replyRepository = null;

        if ("FREE".equals(boardType)) {
            replyRepository = freereplyrepository;
        } else if ("NOTICE".equals(boardType)) {
            replyRepository = noticereplyrepository;
        } else if ("REPORT".equals(boardType)) {
            replyRepository = reportreplyrepository;
        }
        String writer = replyRepository.getWriterOfReply(rno);
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser.getUsername().equals(writer)||currentUser.getUsername().equals("darkest0722@gmail.com")) {
            commentDTO.setRno(rno); // 번호를 일치시킴

            commentService.modify(boardType, commentDTO);

            Map<String, Long> resultMap = new HashMap<>();
            resultMap.put("rno", rno);

            return ResponseEntity.ok(resultMap);
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to delete this replies");
        }

    }




//    @Operation(summary = "GET 방식으로 특정 댓글 조회")
//    @GetMapping("/free/{rno}")
//    public ReplyDTO FreeGetReplyDTO( @PathVariable("rno") Long rno ){
//
//        ReplyDTO replyDTO = freeReplyService.read(rno);
//
//        return replyDTO;
//    }
//    @PostMapping(value = "/free/register", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public Map<String,Long> FreeRegister(
//            @Valid @RequestBody ReplyDTO replyDTO,
//            BindingResult bindingResult)throws BindException {
//
//        log.info(replyDTO);
//
//        if(bindingResult.hasErrors()){
//            throw new BindException(bindingResult);
//        }
//
//        Map<String, Long> resultMap = new HashMap<>();
//
//        Long rno = freeReplyService.register(replyDTO);
//
//        resultMap.put("rno",rno);
//
//        return resultMap;
//    }
//    @Operation(summary = "Replies of Board")
//    @GetMapping(value = "/free/list/{bno}") // 컨트롤러 테스트 !
//    public PageResponseDTO<ReplyDTO> FreeGetList(@PathVariable("bno") Long bno,
//                                                 PageRequestDTO pageRequestDTO){
//
//        PageResponseDTO<ReplyDTO> responseDTO = freeReplyService.getListOfBoard(bno, pageRequestDTO);
//
//        return responseDTO;
//    }

//    @Operation(summary =  "DELETE 방식으로 특정 댓글 삭제")
//    @DeleteMapping("/free/{rno}")
//    public Map<String,Long> FreeRemove( @PathVariable("rno") Long rno ){
//
//        freeReplyService.remove(rno);
//
//        Map<String, Long> resultMap = new HashMap<>();
//
//        resultMap.put("rno", rno);
//
//        return resultMap;
//    }
//    @Operation(summary =  "PUT 방식으로 특정 댓글 수정")
//    @PutMapping(value = "/free/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE )
//    public Map<String,Long> FreeModify( @PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO ){
//
//        replyDTO.setRno(rno); //번호를 일치시킴
//
//        freeReplyService.modify(replyDTO);
//
//        Map<String, Long> resultMap = new HashMap<>();
//
//        resultMap.put("rno", rno);
//
//        return resultMap;
//    }

}
