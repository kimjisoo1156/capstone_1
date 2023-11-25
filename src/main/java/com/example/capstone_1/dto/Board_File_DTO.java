package com.example.capstone_1.dto;

import com.example.capstone_1.domain.BoardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class Board_File_DTO {

    private Long bno;

    @NotBlank
    //@Size(min = 3, max = 100)
    private String title;

    @NotEmpty
    private String content;

    private String writer;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    //첨부파일의 이름들
    private List<String> fileNames;

    private Long secret;
}
