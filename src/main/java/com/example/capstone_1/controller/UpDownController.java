package com.example.capstone_1.controller;


import com.example.capstone_1.dto.UploadFileDTO;
import com.example.capstone_1.dto.UploadResultDTO;
import com.example.capstone_1.utill.S3Uploader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@RestController
@Log4j2
public class UpDownController {
    private final S3Uploader s3Uploader;

    public UpDownController(S3Uploader s3Uploader) {
        this.s3Uploader = s3Uploader;
    }

    @Value("${com.example.upload.path}")
    private String uploadPath;

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
//    @Value("${com.example.upload.path}")// import 시에 springframework으로 시작하는 Value
//    private String uploadPath;
//
//    @Operation(summary =  "POST 방식으로 파일 등록")
//    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public List<UploadResultDTO> upload(
//            @Parameter(
//                    description = "Files to be uploaded",
//                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
//            )
//            UploadFileDTO uploadFileDTO){
//
//        log.info(uploadFileDTO);
//
//        if(uploadFileDTO.getFiles() != null){
//
//            final List<UploadResultDTO> list = new ArrayList<>();
//
//            uploadFileDTO.getFiles().forEach(multipartFile -> {
//
//                String originalName = multipartFile.getOriginalFilename();
//                log.info(originalName);
//
//                String uuid = UUID.randomUUID().toString();
//
//                Path savePath = Paths.get(uploadPath, uuid+"_"+ originalName);
//
//                boolean image = false;
//
//                try {
//                    multipartFile.transferTo(savePath);
//
//                    //이미지 파일의 종류라면
//                    if(Files.probeContentType(savePath).startsWith("image")){
//
//                        image = true;
//
//                        File thumbFile = new File(uploadPath, "s_" + uuid+"_"+ originalName);
//
//                        Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200,200);
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                list.add(UploadResultDTO.builder()
//                        .uuid(uuid)
//                        .fileName(originalName)
//                        .img(image).build()
//                );
//
//
//
//            });//end each
//
//            return list;
//        }//end if
//
//        return null;
//    }


    @Operation(summary =  "GET방식으로 첨부파일 조회")
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName){

        Resource resource = new FileSystemResource(uploadPath+File.separator + fileName);
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();

        try{
            headers.add("Content-Type", Files.probeContentType( resource.getFile().toPath() ));
        } catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @Operation(summary = "DELETE 방식으로 파일 삭제")
    @DeleteMapping("/remove/{fileName}")
    public Map<String,Boolean> removeFile(@PathVariable String fileName){

        Resource resource = new FileSystemResource(uploadPath+File.separator + fileName);
        String resourceName = resource.getFilename();

        Map<String, Boolean> resultMap = new HashMap<>();
        boolean removed = false;

        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();

            //섬네일이 존재한다면
            if(contentType.startsWith("image")){
                File thumbnailFile = new File(uploadPath+File.separator +"s_" + fileName);
                thumbnailFile.delete();
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        resultMap.put("result", removed);

        return resultMap;
    }

}
