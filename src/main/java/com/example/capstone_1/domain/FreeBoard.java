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

    private Long secret;

//    @Column(columnDefinition = "int default 0", nullable = false)	// 조회수의 기본 값을 0으로 지정, null 불가 처리
//    private int viewCount;

    public void changeFreeBoard(String title, String content, Long secret){
        this.title = title;
        this.content = content;
        this.secret = secret;
    }

    // 작성자를 알 수 없음으로 변경하는 메서드
    public void setWriterToUnknown() {
        this.writer = "알수없음";
    }
//    public void setViewCount(Integer viewCount) {
//        if (viewCount != null) {
//            this.viewCount = viewCount;
//        } else {
//            // 기본값이나 다른 적절한 값으로 처리
//            this.viewCount = 0;
//        }
//    }
//
//    public void increaseViewCount() {
//        this.viewCount++;
//    }

}
