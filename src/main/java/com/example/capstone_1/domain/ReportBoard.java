package com.example.capstone_1.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageSetReportBoard")
public class ReportBoard extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column(length = 500, nullable = false) //컬럼의 길이와 null허용여부
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    public void changeReportBoard(String title, String content){
        this.title = title;
        this.content = content;
    }


    @OneToMany(mappedBy = "reportBoard",
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @Builder.Default
    @BatchSize(size = 20)
    private Set<BoardImage> imageSetReportBoard = new HashSet<>();

    public void addImageRepoartBoard(String uuid, String imageUrl){

        BoardImage boardImage = BoardImage.builder()
                .uuid(uuid)
                .imageUrl(imageUrl)
                .reportBoard(this)
                .ord(imageSetReportBoard.size())
                .build();
        imageSetReportBoard.add(boardImage);
    }

    public void clearImagesReportBoard() {

        imageSetReportBoard.forEach(boardImage -> boardImage.changeBoardReportBoard(null));

        this.imageSetReportBoard.clear();
    }
}
