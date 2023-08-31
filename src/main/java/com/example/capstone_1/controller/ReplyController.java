package com.example.capstone_1.controller;

import com.example.capstone_1.dto.PageRequestDTO;
import com.example.capstone_1.dto.PageResponseDTO;
import com.example.capstone_1.dto.ReplyDTO;
import com.example.capstone_1.service.CommentService;
import com.example.capstone_1.service.ReplyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/replies")
@Log4j2
@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
public class ReplyController {

    private final ReplyService freeReplyService;

    private final CommentService commentService;
    public ReplyController(@Qualifier("freeReplyServiceImpl") ReplyService freeReplyService, CommentService commentService) {
        this.freeReplyService = freeReplyService;
        this.commentService = commentService;
    }



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
    @DeleteMapping("/{boardType}/{rno}")
    public ResponseEntity<Map<String, Long>> removeComment(
            @PathVariable String boardType,
            @PathVariable Long rno) {

        commentService.remove(boardType, rno);

        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno", rno);

        return ResponseEntity.ok(resultMap);
    }

    @PutMapping("/{boardType}/{rno}")
    public ResponseEntity<Map<String, Long>> modifyComment(
            @PathVariable String boardType,
            @PathVariable Long rno,
            @RequestBody ReplyDTO commentDTO) {

        commentDTO.setRno(rno); // 번호를 일치시킴

        commentService.modify(boardType, commentDTO);

        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno", rno);

        return ResponseEntity.ok(resultMap);
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
