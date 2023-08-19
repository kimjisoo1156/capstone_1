package com.example.capstone_1.controller;

import com.example.capstone_1.dto.PageRequestDTO;
import com.example.capstone_1.dto.PageResponseDTO;
import com.example.capstone_1.dto.ReplyDTO;
import com.example.capstone_1.service.ReplyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
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


    public ReplyController(@Qualifier("freeReplyServiceImpl") ReplyService freeReplyService) {
        this.freeReplyService = freeReplyService;
    }


    @PostMapping(value = "/free/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Long> FreeRegister(
            @Valid @RequestBody ReplyDTO replyDTO,
            BindingResult bindingResult)throws BindException {

        log.info(replyDTO);

        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }

        Map<String, Long> resultMap = new HashMap<>();

        Long rno = freeReplyService.register(replyDTO);

        resultMap.put("rno",rno);

        return resultMap;
    }
    @Operation(summary = "Replies of Board")
    @GetMapping(value = "/free/list/{bno}") //에러남
    public PageResponseDTO<ReplyDTO> FreeGetList(@PathVariable("bno") Long bno,
                                                 PageRequestDTO pageRequestDTO){

        PageResponseDTO<ReplyDTO> responseDTO = freeReplyService.getListOfBoard(bno, pageRequestDTO);

        return responseDTO;
    }
    @Operation(summary = "GET 방식으로 특정 댓글 조회")
    @GetMapping("/free/{rno}")
    public ReplyDTO FreeGetReplyDTO( @PathVariable("rno") Long rno ){

        ReplyDTO replyDTO = freeReplyService.read(rno);

        return replyDTO;
    }
    @Operation(summary =  "DELETE 방식으로 특정 댓글 삭제")
    @DeleteMapping("/free/{rno}")
    public Map<String,Long> FreeRemove( @PathVariable("rno") Long rno ){

        freeReplyService.remove(rno);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("rno", rno);

        return resultMap;
    }
    @Operation(summary =  "PUT 방식으로 특정 댓글 수정")
    @PutMapping(value = "/free/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE )
    public Map<String,Long> FreeModify( @PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO ){

        replyDTO.setRno(rno); //번호를 일치시킴

        freeReplyService.modify(replyDTO);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("rno", rno);

        return resultMap;
    }

}
