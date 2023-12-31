package com.example.capstone_1.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageSetNoticeBoard")
public class NoticeBoard extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column(length = 500, nullable = false) //컬럼의 길이와 null허용여부
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    @OneToMany(mappedBy = "noticeBoard", cascade = CascadeType.ALL)
    private List<FileEntity> files;

    private Long secret;
    public void changeNoticeBoard(String title, String content, Long secret){
        this.title = title;
        this.content = content;
        this.secret = secret;
    }

    public void setWriterToUnknown() {
        this.writer = "알수없음";
    }
}
