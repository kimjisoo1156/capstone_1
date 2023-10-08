package com.example.capstone_1.service;

import com.example.capstone_1.domain.BoardType;
import com.example.capstone_1.domain.FileEntity;
import com.example.capstone_1.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    public FileEntity save(FileEntity fileEntity) {
        return fileRepository.save(fileEntity);
    }

    public List<FileEntity> getFiles() {
        List<FileEntity> all = fileRepository.findAll();
        return all;
    }
    public FileEntity getFileById(Long id) {
        return fileRepository.findById(id).orElse(null);
    }

    public void deleteFileById(Long id) {
        fileRepository.deleteById(id);
    }
    // 게시판 타입과 게시물 번호를 기반으로 이미지를 가져오는 메서드
    public List<FileEntity> getImagesForBoard(BoardType boardType, Long bno) {
        return fileRepository.findImagesForBoard(boardType, bno);
    }
}
