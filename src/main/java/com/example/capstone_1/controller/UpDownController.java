package com.example.capstone_1.controller;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.capstone_1.dto.UploadFileDTO;
import com.example.capstone_1.dto.UploadResultDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


@RestController
@Log4j2
public class UpDownController {

    private final AmazonS3 amazonS3;

    @Autowired
    public UpDownController(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Operation(summary =  "POST 방식으로 파일 등록")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDTO> upload(UploadFileDTO uploadFileDTO) throws IOException {
            log.info(uploadFileDTO);

            if (uploadFileDTO.getFiles() != null) {
                final List<UploadResultDTO> list = new ArrayList<>();

                for (MultipartFile multipartFile : uploadFileDTO.getFiles()) {
                    String originalName = multipartFile.getOriginalFilename();
                    log.info(originalName);

                    String uuid = UUID.randomUUID().toString();
                    String fileName = uuid + "_" + originalName;

                    // S3에 업로드
                    PutObjectRequest putObjectRequest = new PutObjectRequest("caspton-bucket", fileName, multipartFile.getInputStream(), null);
                    amazonS3.putObject(putObjectRequest);

                    String imageUrl = "https://caspton-bucket.s3.amazonaws.com/" + fileName;

                    list.add(UploadResultDTO.builder()
                            .uuid(uuid)
                            .fileName(originalName)
                            .img(true)
                            .imageUrl(imageUrl) // 이미지 URL 추가
                            .build()
                    );
                }

                return list;
        }

        return null;
    }

    @Operation(summary = "GET방식으로 첨부파일 조회")
    @GetMapping("/view/{fileName:.+}") // 확장자를 포함한 파일 이름에 대한 패턴 지정
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName) throws IOException {
        // S3에서 파일을 읽어오기 위해 S3 객체의 getObject 메서드를 사용
        S3Object s3Object = amazonS3.getObject("caspton-bucket", fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();

        // 파일 이름으로 부터 확장자 추출
        String[] splitFileName = fileName.split("\\.");
        String fileExtension = splitFileName[splitFileName.length - 1];

        // HttpHeaders에 Content-Type 지정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", Files.probeContentType(Paths.get(fileName)));

        // ResponseEntity를 사용하여 파일 스트림과 헤더를 반환
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(s3Object.getObjectMetadata().getContentLength())
                .body(new InputStreamResource(inputStream));
    }


    @Operation(summary = "DELETE 방식으로 파일 삭제")
    @DeleteMapping("/remove/{fileName:.+}") // 확장자를 포함한 파일 이름에 대한 패턴 지정
    public Map<String, Boolean> removeFile(@PathVariable String fileName) {
        Map<String, Boolean> resultMap = new HashMap<>();
        boolean removed = false;

        try {
            // S3에서 파일 삭제
            amazonS3.deleteObject("caspton-bucket", fileName);

            // 삭제 결과를 resultMap에 저장
            removed = true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        resultMap.put("result", removed);
        return resultMap;
    }

}
