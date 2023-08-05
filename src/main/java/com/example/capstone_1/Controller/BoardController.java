package com.example.capstone_1.controller;

import com.example.capstone_1.dto.BoardDTO;
import com.example.capstone_1.dto.BoardListAllDTO;
import com.example.capstone_1.dto.PageRequestDTO;
import com.example.capstone_1.dto.PageResponseDTO;
import com.example.capstone_1.service.FreeBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    @Value("${com.example.upload.path}")// import 시에 springframework으로 시작하는 Value
    private String uploadPath;

    private final FreeBoardService freeBoardService;
//    private final ReportBoardService reportBoardService;


    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){

        //PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        PageResponseDTO<BoardListAllDTO> responseDTO =
                freeBoardService.listWithAll(pageRequestDTO);

        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO);
    }


    @GetMapping("/register")
    public void registerGET(){

    }

    @PostMapping("/register")
    public String registerPost(@Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        log.info("board POST register.......");

        if(bindingResult.hasErrors()) {
            log.info("has errors.......");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );
            return "redirect:/board/register";
        }

        log.info(boardDTO);

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
                          RedirectAttributes redirectAttributes){

        log.info("board modify post......." + boardDTO);

        if(bindingResult.hasErrors()) {
            log.info("has errors.......");

            String link = pageRequestDTO.getLink();

            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );

            redirectAttributes.addAttribute("bno", boardDTO.getBno());

            return "redirect:/board/modify?"+link;
        }

        freeBoardService.modify(boardDTO);

        redirectAttributes.addFlashAttribute("result", "modified");

        redirectAttributes.addAttribute("bno", boardDTO.getBno());

        return "redirect:/board/read";
    }

    @PostMapping("/remove")   //댓글 추가 되면 게시판 삭제가 안됌 !! 실제 api는 잘 동작 됨
    public String remove(BoardDTO boardDTO, RedirectAttributes redirectAttributes) {

        Long bno  = boardDTO.getBno();
        log.info("remove post.. " + bno);

        freeBoardService.remove(bno);

        //게시물이 삭제되었다면 첨부 파일 삭제
        log.info(boardDTO.getFileNames());
        List<String> fileNames = boardDTO.getFileNames();
        if(fileNames != null && fileNames.size() > 0){
            removeFiles(fileNames);
        }

        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/board/list";

    }


    public void removeFiles(List<String> files){

        for (String fileName:files) {

            Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
            String resourceName = resource.getFilename();


            try {
                String contentType = Files.probeContentType(resource.getFile().toPath());
                resource.getFile().delete();

                //섬네일이 존재한다면
                if (contentType.startsWith("image")) {
                    File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName);
                    thumbnailFile.delete();
                }

            } catch (Exception e) {
                log.error(e.getMessage());
            }

        }//end for
    }
//
//    @GetMapping("/list")
//    public void list(PageRequestDTO pageRequestDTO, Model model){
//
//        //PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
//
//        PageResponseDTO<BoardListAllDTO> responseDTO =
//                reportBoardService.listWithAll(pageRequestDTO);
//
//        log.info(responseDTO);
//
//        model.addAttribute("responseDTO", responseDTO);
//    }
//
//
//    @GetMapping("/register")
//    public void registerGET(){
//
//    }
//
//    @PostMapping("/register")
//    public String registerPost(@Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){
//
//        log.info("board POST register.......");
//
//        if(bindingResult.hasErrors()) {
//            log.info("has errors.......");
//            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );
//            return "redirect:/board/register";
//        }
//
//        log.info(boardDTO);
//
//        Long bno  = reportBoardService.register(boardDTO);
//
//        redirectAttributes.addFlashAttribute("result", bno);
//
//        return "redirect:/board/list";
//    }
//
//
//    @GetMapping({"/read", "/modify"})
//    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model){
//
//        BoardDTO boardDTO = reportBoardService.readOne(bno);
//
//        log.info(boardDTO);
//
//        model.addAttribute("dto", boardDTO);
//
//    }
//
//    @PostMapping("/modify")
//    public String modify( @Valid BoardDTO boardDTO,
//                          BindingResult bindingResult,
//                          PageRequestDTO pageRequestDTO,
//                          RedirectAttributes redirectAttributes){
//
//        log.info("board modify post......." + boardDTO);
//
//        if(bindingResult.hasErrors()) {
//            log.info("has errors.......");
//
//            String link = pageRequestDTO.getLink();
//
//            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );
//
//            redirectAttributes.addAttribute("bno", boardDTO.getBno());
//
//            return "redirect:/board/modify?"+link;
//        }
//
//        reportBoardService.modify(boardDTO);
//
//        redirectAttributes.addFlashAttribute("result", "modified");
//
//        redirectAttributes.addAttribute("bno", boardDTO.getBno());
//
//        return "redirect:/board/read";
//    }
//
//    @PostMapping("/remove")
//    public String remove(BoardDTO boardDTO, RedirectAttributes redirectAttributes) {
//
//        Long bno  = boardDTO.getBno();
//        log.info("remove post.. " + bno);
//
//        reportBoardService.remove(bno);
//
//        //게시물이 삭제되었다면 첨부 파일 삭제
//        log.info(boardDTO.getFileNames());
//        List<String> fileNames = boardDTO.getFileNames();
//        if(fileNames != null && fileNames.size() > 0){
//            removeFiles(fileNames);
//        }
//
//        redirectAttributes.addFlashAttribute("result", "removed");
//
//        return "redirect:/board/list";
//
//    }
//
//
//    public void removeFiles(List<String> files){
//
//        for (String fileName:files) {
//
//            Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
//            String resourceName = resource.getFilename();
//
//
//            try {
//                String contentType = Files.probeContentType(resource.getFile().toPath());
//                resource.getFile().delete();
//
//                //섬네일이 존재한다면
//                if (contentType.startsWith("image")) {
//                    File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName);
//                    thumbnailFile.delete();
//                }
//
//            } catch (Exception e) {
//                log.error(e.getMessage());
//            }
//
//        }//end for
//    }




}
