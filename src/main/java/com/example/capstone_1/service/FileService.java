package com.example.capstone_1.service;

import com.example.capstone_1.domain.FileEntity;
import com.example.capstone_1.dto.FileDto;
import com.example.capstone_1.dto.FileResponseDto;
import com.example.capstone_1.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    public void save(FileResponseDto fileResponseDto) { //db에 저장하는 부분
        FileEntity fileEntity = new FileEntity(fileResponseDto.getFileName(), fileResponseDto.getUuid(), fileResponseDto.getUrl());
        fileRepository.save(fileEntity);
    }

    public List<FileEntity> getFiles() {
        List<FileEntity> all = fileRepository.findAll();
        return all;
    }
}
