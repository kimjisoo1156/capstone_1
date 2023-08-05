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
@ToString(exclude = "imageSetBankBoard")
public class BankBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column(length = 500, nullable = false) //컬럼의 길이와 null허용여부
    private String title;

    @Column(length = 100, nullable = false)
    private String accountHolder; // 예금주

    @Column(length = 100, nullable = false)
    private String bankName; // 은행이름

    @Column(length = 100, nullable = false)
    private String accountNumber; // 계좌번호

    @Column(length = 2000, nullable = false)
    private String content;  //입금확인 부탁드립니다 ^^

    @Column(length = 50, nullable = false)
    private String writer;

    public void changeBankBoard(String title, String bankName, String accountNumber,String accountHolder  ,String content){ //수정할 내용
        this.title = title;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.content = content;
    }

    @OneToMany(mappedBy = "bankBoard",
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @Builder.Default
    @BatchSize(size = 20)
    private Set<BoardImage> imageSetBankBoard = new HashSet<>();

    public void addImageBankBoard(String uuid, String fileName){

        BoardImage boardImage = BoardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .bankBoard(this)
                .ord(imageSetBankBoard.size())
                .build();
        imageSetBankBoard.add(boardImage);
    }

    public void clearImagesBankBoard() {

        imageSetBankBoard.forEach(boardImage -> boardImage.changeBoardBankBoard(null));

        this.imageSetBankBoard.clear();
    }
}
