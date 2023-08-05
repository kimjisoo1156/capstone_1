package com.example.capstone_1.domain;


import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value = { AuditingEntityListener.class })
@Getter
abstract class BaseEntity {

    @CreatedDate
    @Column(name = "regdate", updatable = false) //등록 날짜 시간
    private LocalDateTime regDate;

    @LastModifiedDate
    @Column(name ="moddate" ) //수정 날짜 시간
    private LocalDateTime modDate;

    @Column(name="is_secret")
    private boolean isSecret; //게시판 공개 여부

}
