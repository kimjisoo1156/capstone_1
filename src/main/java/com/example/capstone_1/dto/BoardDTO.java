package com.example.capstone_1.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BoardDTO {

    private Long bno;

    @NotBlank
    //@Size(min = 3, max = 100)
    private String title;

    @NotEmpty
    private String content;

    private String writer; //<-이것도 회원의 아이디가 와야함.

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    //첨부파일의 이름들
    private List<String> fileNames;

    private boolean isSecret;

    private List<UploadFileDTO> uploadFiles;

}
