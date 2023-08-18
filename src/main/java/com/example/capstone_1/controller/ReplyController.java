//package com.example.capstone_1.controller;
//
//
//import com.example.capstone_1.dto.PageRequestDTO;
//import com.example.capstone_1.dto.PageResponseDTO;
//import com.example.capstone_1.dto.ReplyDTO;
//import com.example.capstone_1.service.ReplyService;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.validation.BindException;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//import jakarta.validation.Valid;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/replies")
//@Log4j2
//@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
//public class ReplyController {
//
//    private final ReplyService freeReplyService;
//    private final ReplyService reportReplyService;
//    private final ReplyService noticeReplyService;
//    private final ReplyService bankReplyService;
//
//    public ReplyController(@Qualifier("freeReplyServiceImpl") ReplyService freeReplyService,
//                           @Qualifier("reportReplyServiceImpl") ReplyService reportReplyService,
//                           @Qualifier("noticeReplyServiceImpl") ReplyService noticeReplyService, @Qualifier("bankReplyServiceImpl") ReplyService bankReplyService) {
//        this.freeReplyService = freeReplyService;
//        this.reportReplyService = reportReplyService;
//        this.noticeReplyService = noticeReplyService;
//        this.bankReplyService = bankReplyService;
//    }
//
//
//    @PostMapping(value = "/free/register", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public Map<String,Long> FreeRegister(
//            @Valid @RequestBody ReplyDTO replyDTO,
//            BindingResult bindingResult)throws BindException{
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
//
//    @PostMapping(value = "/report/register", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public Map<String,Long> ReportRegister(
//            @Valid @RequestBody ReplyDTO replyDTO,
//            BindingResult bindingResult)throws BindException{
//
//        log.info(replyDTO);
//
//        if(bindingResult.hasErrors()){
//            throw new BindException(bindingResult);
//        }
//
//        Map<String, Long> resultMap = new HashMap<>();
//
//        Long rno = reportReplyService.register(replyDTO);
//
//        resultMap.put("rno",rno);
//
//        return resultMap;
//    }
//
//    @PostMapping(value = "/notice/register", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public Map<String,Long> NoticeRegister(
//            @Valid @RequestBody ReplyDTO replyDTO,
//            BindingResult bindingResult)throws BindException{
//
//        log.info(replyDTO);
//
//        if(bindingResult.hasErrors()){
//            throw new BindException(bindingResult);
//        }
//
//        Map<String, Long> resultMap = new HashMap<>();
//
//        Long rno = noticeReplyService.register(replyDTO);
//
//        resultMap.put("rno",rno);
//
//        return resultMap;
//    }
//
//    @PostMapping(value = "/bank/register", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public Map<String,Long> BankRegister(
//            @Valid @RequestBody ReplyDTO replyDTO,
//            BindingResult bindingResult)throws BindException{
//
//        log.info(replyDTO);
//
//        if(bindingResult.hasErrors()){
//            throw new BindException(bindingResult);
//        }
//
//        Map<String, Long> resultMap = new HashMap<>();
//
//        Long rno = bankReplyService.register(replyDTO);
//
//        resultMap.put("rno",rno);
//
//        return resultMap;
//    }
//
//    //http://localhost:8080/replies/
////    {
////        "bno": 1,
////            "replyText":"댓글",
////            "replyer": "user"
////
////    }
//
//
//
////    http://localhost:8080/replies/list/1  //특정 게시물의 댓글 목록 뒤에 페이지 번호를 추가해서 댓글 페이징 처리..??
////
////    {
////        "page": 1,
////            "size": 10,
////            "total": 1,
////            "start": 1,
////            "end": 1,
////            "prev": false,
////            "next": false,
////            "dtoList": [
////        {
////            "rno": 1,
////                "bno": 1,
////                "replyText": "안녕댓글하이",
////                "replyer": "user",
////                "regDate": "2023-07-27 12:28:45"
////        }
////    ]
////    }
//
//    @Operation(summary = "Replies of Board")
//    @GetMapping(value = "/free/list/{bno}")
//    public PageResponseDTO<ReplyDTO> FreeGetList(@PathVariable("bno") Long bno,
//                                             PageRequestDTO pageRequestDTO){
//
//        PageResponseDTO<ReplyDTO> responseDTO = freeReplyService.getListOfBoard(bno, pageRequestDTO);
//
//        return responseDTO;
//    }
//
//    @Operation(summary = "Replies of Board")
//    @GetMapping(value = "/report/list/{bno}")
//    public PageResponseDTO<ReplyDTO> ReportGetList(@PathVariable("bno") Long bno,
//                                                   PageRequestDTO pageRequestDTO){
//
//        PageResponseDTO<ReplyDTO> responseDTO = reportReplyService.getListOfBoard(bno, pageRequestDTO);
//
//        return responseDTO;
//    }
//    @Operation(summary = "Replies of Board")
//    @GetMapping(value = "/notice/list/{bno}")
//    public PageResponseDTO<ReplyDTO> NoticeGetList(@PathVariable("bno") Long bno,
//                                                   PageRequestDTO pageRequestDTO){
//
//        PageResponseDTO<ReplyDTO> responseDTO = noticeReplyService.getListOfBoard(bno, pageRequestDTO);
//
//        return responseDTO;
//    }
//    @Operation(summary = "Replies of Board")
//    @GetMapping(value = "/bank/list/{bno}")
//    public PageResponseDTO<ReplyDTO> BankGetList(@PathVariable("bno") Long bno,
//                                                   PageRequestDTO pageRequestDTO){
//
//        PageResponseDTO<ReplyDTO> responseDTO = bankReplyService.getListOfBoard(bno, pageRequestDTO);
//
//        return responseDTO;
//    }
//
//
//    //http://localhost:8080/replies/1
//
////    {
////        "rno": 1,
////            "bno": 1,
////            "replyText": "댓글",
////            "replyer": "user",
////            "regDate": "2023-07-27 12:28:45"
////    }
//    @Operation(summary = "GET 방식으로 특정 댓글 조회")
//    @GetMapping("/free/{rno}")
//    public ReplyDTO FreeGetReplyDTO( @PathVariable("rno") Long rno ){
//
//        ReplyDTO replyDTO = freeReplyService.read(rno);
//
//        return replyDTO;
//    }
//    @Operation(summary = "GET 방식으로 특정 댓글 조회")
//    @GetMapping("/report/{rno}")
//    public ReplyDTO ReportGetReplyDTO( @PathVariable("rno") Long rno ){
//
//        ReplyDTO replyDTO = reportReplyService.read(rno);
//
//        return replyDTO;
//    }
//    @Operation(summary = "GET 방식으로 특정 댓글 조회")
//    @GetMapping("/notice/{rno}")
//    public ReplyDTO NoticeGetReplyDTO( @PathVariable("rno") Long rno ){
//
//        ReplyDTO replyDTO = noticeReplyService.read(rno);
//
//        return replyDTO;
//    }
//    @Operation(summary = "GET 방식으로 특정 댓글 조회")
//    @GetMapping("/bank/{rno}")
//    public ReplyDTO BankGetReplyDTO( @PathVariable("rno") Long rno ){
//
//        ReplyDTO replyDTO = bankReplyService.read(rno);
//
//        return replyDTO;
//    }
//
//
////    http://localhost:8080/replies/2
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
//
//    @Operation(summary =  "DELETE 방식으로 특정 댓글 삭제")
//    @DeleteMapping("/report/{rno}")
//    public Map<String,Long> ReportRemove( @PathVariable("rno") Long rno ){
//
//        reportReplyService.remove(rno);
//
//        Map<String, Long> resultMap = new HashMap<>();
//
//        resultMap.put("rno", rno);
//
//        return resultMap;
//    }
//    @Operation(summary =  "DELETE 방식으로 특정 댓글 삭제")
//    @DeleteMapping("/notice/{rno}")
//    public Map<String,Long> NoticeRemove( @PathVariable("rno") Long rno ){
//
//        noticeReplyService.remove(rno);
//
//        Map<String, Long> resultMap = new HashMap<>();
//
//        resultMap.put("rno", rno);
//
//        return resultMap;
//    }
//    @Operation(summary =  "DELETE 방식으로 특정 댓글 삭제")
//    @DeleteMapping("/bank/{rno}")
//    public Map<String,Long> BankRemove( @PathVariable("rno") Long rno ){
//
//        bankReplyService.remove(rno);
//
//        Map<String, Long> resultMap = new HashMap<>();
//
//        resultMap.put("rno", rno);
//
//        return resultMap;
//    }
//
//
//    //    http://localhost:8080/replies/1
////    {
////        "replyText": "안녕댓글하이"
////    }
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
//    @Operation(summary =  "PUT 방식으로 특정 댓글 수정")
//    @PutMapping(value = "/report/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE )
//    public Map<String,Long> ReportModify( @PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO ){
//
//        replyDTO.setRno(rno); //번호를 일치시킴
//
//        reportReplyService.modify(replyDTO);
//
//        Map<String, Long> resultMap = new HashMap<>();
//
//        resultMap.put("rno", rno);
//
//        return resultMap;
//    }
//    @Operation(summary =  "PUT 방식으로 특정 댓글 수정")
//    @PutMapping(value = "/notice/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE )
//    public Map<String,Long> NoticeModify( @PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO ){
//
//        replyDTO.setRno(rno); //번호를 일치시킴
//
//        noticeReplyService.modify(replyDTO);
//
//        Map<String, Long> resultMap = new HashMap<>();
//
//        resultMap.put("rno", rno);
//
//        return resultMap;
//    }
//    @Operation(summary =  "PUT 방식으로 특정 댓글 수정")
//    @PutMapping(value = "/bank/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE )
//    public Map<String,Long> BankModify( @PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO ){
//
//        replyDTO.setRno(rno); //번호를 일치시킴
//
//        bankReplyService.modify(replyDTO);
//
//        Map<String, Long> resultMap = new HashMap<>();
//
//        resultMap.put("rno", rno);
//
//        return resultMap;
//    }
//
//
//
//
//}
