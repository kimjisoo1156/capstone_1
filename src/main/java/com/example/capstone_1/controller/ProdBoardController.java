package com.example.capstone_1.controller;


import com.example.capstone_1.dto.BoardDTO;
import com.example.capstone_1.dto.BoardListAllDTO;
import com.example.capstone_1.dto.PageRequestDTO;
import com.example.capstone_1.dto.PageResponseDTO;
import com.example.capstone_1.service.FreeBoardService;
import com.example.capstone_1.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class ProdBoardController {

    // AWS S3Uploader 추가
    private final S3Uploader s3Uploader;

    @Value("${cloud.aws.s3.bucket}") // AWS S3 버킷 이름으로 변경
    private String bucketName;


    private final FreeBoardService freeBoardService;


    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){


        PageResponseDTO<BoardListAllDTO> responseDTO =
                freeBoardService.listWithAll(pageRequestDTO);

        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO);
    }


    @GetMapping("/register")
    public void registerGET(){

    }

    @PostMapping("/register")
    public String registerPost(@Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, @RequestParam("files") MultipartFile[] files) throws IOException {

        log.info("board POST register.......");

        if(bindingResult.hasErrors()) {
            log.info("has errors.......");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );
            return "redirect:/board/register";
        }

        log.info(boardDTO);

        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String s3Url = s3Uploader.upload(file, "uploads"); // S3로 파일 업로드
            fileUrls.add(s3Url);
        }
        boardDTO.setFileNames(fileUrls);

        Long bno  = freeBoardService.register(boardDTO);

        redirectAttributes.addFlashAttribute("result", bno);

        return "redirect:/board/list";
    }

    @GetMapping({"/read", "/modify"})
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model){

        BoardDTO boardDTO = freeBoardService.readOne(bno);

        log.info(boardDTO);

        model.addAttribute("dto", boardDTO);

    }

    @PostMapping("/modify")
    public String modify( @Valid BoardDTO boardDTO,
                          BindingResult bindingResult,
                          PageRequestDTO pageRequestDTO,
                          RedirectAttributes redirectAttributes,
                          @RequestParam("files") MultipartFile[] files) throws IOException {

        log.info("board modify post......." + boardDTO);

        if(bindingResult.hasErrors()) {
            log.info("has errors.......");

            String link = pageRequestDTO.getLink();

            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );

            redirectAttributes.addAttribute("bno", boardDTO.getBno());

            return "redirect:/board/modify?"+link;
        }

        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String s3Url = s3Uploader.upload(file, "uploads"); // S3로 파일 업로드
            fileUrls.add(s3Url);
        }
        boardDTO.setFileNames(fileUrls); // 수정: fileUrls로 설정

        freeBoardService.modify(boardDTO);

        redirectAttributes.addFlashAttribute("result", "modified");

        redirectAttributes.addAttribute("bno", boardDTO.getBno());

        return "redirect:/board/read";
    }


    @PostMapping("/remove")
    public String remove(BoardDTO boardDTO, RedirectAttributes redirectAttributes) {

        Long bno  = boardDTO.getBno();
        log.info("remove post.. " + bno);

        freeBoardService.remove(bno);

        List<String> fileUrls = boardDTO.getFileNames();
        if (fileUrls != null && !fileUrls.isEmpty()) {
            for (String fileUrl : fileUrls) {
                String fileName = s3Uploader.getFileNameFromUrl(fileUrl);
                s3Uploader.deleteFile(fileName); // S3에서 파일 삭제
            }
        }

        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/board/list";
    }



}
