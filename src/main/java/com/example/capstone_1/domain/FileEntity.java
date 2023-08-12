package com.example.capstone_1.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String fileName;

    @Column
    private String uuid;

    @Column
    private String s3Url;

    public FileEntity(String fileName,String uuid, String s3Url) {
        this.fileName = fileName;
        this.uuid = uuid;
        this.s3Url = s3Url;
    }

}
