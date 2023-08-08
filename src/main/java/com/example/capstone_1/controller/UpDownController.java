package com.example.capstone_1.controller;


import com.example.capstone_1.dto.UploadFileDTO;
import com.example.capstone_1.dto.UploadResultDTO;
import com.example.capstone_1.service.S3Uploader;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;


@RestController
@Log4j2
public class UpDownController {
    private final S3Uploader s3Uploader;

    public UpDownController(S3Uploader s3Uploader) {
        this.s3Uploader = s3Uploader;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDTO> upload(UploadFileDTO uploadFileDTO) {
        List<UploadResultDTO> resultList = new ArrayList<>();

        if (uploadFileDTO.getFiles() != null) {
            uploadFileDTO.getFiles().forEach(multipartFile -> {
                String originalName = multipartFile.getOriginalFilename();
                String uuid = UUID.randomUUID().toString();

                try {
                    // S3로 파일 업로드
                    String s3Url = s3Uploader.upload(multipartFile, "uploads"); // "uploads"는 S3 내의 디렉토리명

                    resultList.add(UploadResultDTO.builder()
                            .uuid(uuid)
                            .fileName(originalName)
                            .img(multipartFile.getContentType().startsWith("image"))
                            .url(s3Url) // S3로 업로드한 파일의 URL
                            .build());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        return resultList;
    }

    @Operation(summary =  "GET방식으로 첨부파일 조회")
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName) {
        try {
            // S3 버킷에서 파일을 가져오기 위해 S3Uploader를 통해 URL을 얻어옴
            String s3Url = s3Uploader.getFileUrl("uploads/" + fileName);

            // 가져온 URL을 이용해 ResponseEntity를 생성하여 반환
            Resource resource = new UrlResource(s3Url);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
            return ResponseEntity.ok().headers(headers).body(resource);
        } catch (Exception e) {
            log.error("Error while retrieving file: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "DELETE 방식으로 파일 삭제")
    @DeleteMapping("/remove/{fileName}")
    public Map<String,Boolean> removeFile(@PathVariable String fileName) {
        Map<String, Boolean> resultMap = new HashMap<>();

        try {
            // S3 버킷에서 파일 삭제
            s3Uploader.deleteFile("uploads/" + fileName);

            // 섬네일 파일도 삭제할 경우 추가 로직이 필요함

            resultMap.put("result", true);
        } catch (Exception e) {
            log.error("Error while removing file: {}", e.getMessage());
            resultMap.put("result", false);
        }

        return resultMap;
    }

}
