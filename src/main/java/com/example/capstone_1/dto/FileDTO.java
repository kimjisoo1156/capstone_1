package com.example.capstone_1.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter

//파일 관련정보
public class FileDTO {
    private MultipartFile file;
}
