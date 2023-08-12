package com.example.capstone_1.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
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
        String fileName = multipartFile.getOriginalFilename();
        String ext = fileName.split("\\.")[1];
        String contentType = "";

        switch (ext) {
            case "jpeg":
                contentType = "image/jpeg";
                break;
            case "png":
                contentType = "image/png";
                break;
            case "txt":
                contentType = "text/plain";
                break;
            case "csv":
                contentType = "text/csv";
                break;
            // 다른 파일 형식에 따른 Content-Type도 추가 가능
        }

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);

        try {
            // MultipartFile을 File로 변환
            File tempFile = convertMultipartFileToFile(multipartFile);

            // S3에 파일 업로드 및 Content-Type 설정
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, tempFile)
                    .withCannedAcl(CannedAccessControlList.PublicRead)
                    .withMetadata(metadata));

            // S3에 업로드한 파일의 URL 생성
            String url = amazonS3.getUrl(bucket, fileName).toString();

            // 임시 파일 삭제
            tempFile.delete();

            FileResponseDto fileResponseDto = new FileResponseDto();
            fileResponseDto.setFileName(fileName);
            fileResponseDto.setUuid(uuid);
            fileResponseDto.setUrl(url);

            return fileResponseDto;
        } catch (SdkClientException e) {
            e.printStackTrace();
            return null; // 업로드 실패 시 예외 처리
        }
    }

    // MultipartFile을 File로 변환하는 메서드
    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File tempFile = File.createTempFile("temp", null);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }
    public void deleteFile(String fileName) {
        try {
            amazonS3.deleteObject(bucket, fileName);
        } catch (AmazonServiceException e) {
            e.printStackTrace();
            // 파일 삭제 실패 시 예외 처리
        }
    }
}
