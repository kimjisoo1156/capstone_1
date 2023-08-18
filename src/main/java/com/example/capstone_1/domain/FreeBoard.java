package com.example.capstone_1.domain;

import lombok.*;
import org.hibernate.annotations.BatchSize;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FreeBoard extends BaseEntity{  //자유게시판
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column(length = 500, nullable = false) //컬럼의 길이와 null허용여부
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    @OneToMany(mappedBy = "freeBoard", cascade = CascadeType.ALL)
    private List<FileEntity> files;

    private String secret;
    public void changeFreeBoard(String title, String content){
        this.title = title;
        this.content = content;
    }

}
