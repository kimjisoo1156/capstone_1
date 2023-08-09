package com.example.capstone_1.domain;

import lombok.*;
import org.hibernate.annotations.BatchSize;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageSetFreeBoard")
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

    public void changeFreeBoard(String title, String content){
        this.title = title;
        this.content = content;
    }

    @OneToMany(mappedBy = "freeBoard",
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @Builder.Default
    @BatchSize(size = 20)
    private Set<BoardImage> imageSetFreeBoard = new HashSet<>();

    public void addImageFreeBoard(String uuid, String imageUrl){

        BoardImage boardImage = BoardImage.builder()
                .uuid(uuid)
                .imageUrl(imageUrl)
                .freeBoard(this)
                .ord(imageSetFreeBoard.size())
                .build();
        imageSetFreeBoard.add(boardImage);
    }

    public void clearImagesFreeBoard() {

        imageSetFreeBoard.forEach(boardImage -> boardImage.changeBoardFreeBoard(null));

        this.imageSetFreeBoard.clear();
    }

}
