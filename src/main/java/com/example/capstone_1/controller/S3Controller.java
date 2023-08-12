package com.example.capstone_1.controller;

import com.example.capstone_1.domain.FileEntity;
import com.example.capstone_1.dto.FileDto;
import com.example.capstone_1.dto.FileResponseDto;
import com.example.capstone_1.service.FileService;
import com.example.capstone_1.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;
    private final FileService fileService;

    @GetMapping("/api/upload")
    public String goToUpload() {
        return "gallery";
    }

    @PostMapping("/api/upload")
    public String uploadFile(FileDto fileDto) throws IOException {
        // S3에 파일 업로드 및 정보 저장
        FileResponseDto fileResponseDto = s3Service.uploadFile(fileDto.getFile());

        // FileResponseDto에서 필요한 정보로 FileEntity 생성
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(fileResponseDto.getFileName());
        fileEntity.setUuid(fileResponseDto.getUuid());
        fileEntity.setS3Url(fileResponseDto.getUrl());

        // FileEntity를 저장
        fileService.save(fileEntity);

        return "redirect:/api/gallery";
    }

    @GetMapping("/api/gallery") //(이미지조회)업로드한 이미지 화면에 보여주는 컨트롤러
    public String listPage(Model model) {
        List<FileEntity> fileList =fileService.getFiles();
        model.addAttribute("fileList", fileList);
        return "gallery";
    }
    @GetMapping("/api/files") //업로드한 파일 정보 프론트에게 json형태로 주는 컨트롤러
    public ResponseEntity<List<FileEntity>> getFiles() {
        List<FileEntity> fileList = fileService.getFiles();
        return ResponseEntity.ok(fileList);
    }
    @PostMapping("/api/edit/{id}") // 파일 수정을 위한 컨트롤러 엔드포인트
    public String editFile(@PathVariable Long id, FileDto fileDto) throws IOException {
        FileEntity fileEntity = fileService.getFileById(id);

        // 기존 파일을 S3에서 삭제
        s3Service.deleteFile(fileEntity.getFileName());

        // 새로운 파일 업로드
        FileResponseDto newFileResponseDto = s3Service.uploadFile(fileDto.getFile());
        fileEntity.setFileName(newFileResponseDto.getFileName());
        fileEntity.setS3Url(newFileResponseDto.getUrl());
        fileService.save(fileEntity);

        return "redirect:/api/gallery";
    }
    @PostMapping("/api/delete/{id}") // 파일 삭제를 위한 컨트롤러 엔드포인트
    public String deleteFile(@PathVariable Long id) {
        FileEntity fileEntity = fileService.getFileById(id);

        // S3에서 파일 삭제
        s3Service.deleteFile(fileEntity.getFileName());

        // 데이터베이스에서 파일 정보 삭제
        fileService.deleteFileById(id);

        return "redirect:/api/gallery";
    }
}