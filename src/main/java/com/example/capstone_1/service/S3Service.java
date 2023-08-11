package com.example.capstone_1.service;

import com.amazonaws.services.s3.AmazonS3;
import com.example.capstone_1.dto.FileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public FileResponseDto uploadFile(MultipartFile multipartFile) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + "_" + multipartFile.getOriginalFilename();

        // MultipartFile을 File로 변환
        File tempFile = convertMultipartFileToFile(multipartFile);

        // S3에 파일 업로드
        amazonS3.putObject(bucket, fileName, tempFile);

        // S3에 업로드한 파일의 URL 생성
        String url = amazonS3.getUrl(bucket, fileName).toString();

        // 임시 파일 삭제
        tempFile.delete();

        FileResponseDto fileResponseDto = new FileResponseDto();
        fileResponseDto.setFileName(fileName);
        fileResponseDto.setUuid(uuid);
        fileResponseDto.setUrl(url);

        return fileResponseDto;
    }

    // MultipartFile을 File로 변환하는 메서드
    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File tempFile = File.createTempFile("temp", null);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }
}
