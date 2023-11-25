package com.example.capstone_1.dto;

import com.example.capstone_1.domain.BoardType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileResponseDTO {
    private Long id;
    private String fileName;
    private String url;
    private String uuid;
    private BoardType boardType;  //게시판 종류
    private Long bno; //게시판 번호


}
