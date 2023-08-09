package com.example.capstone_1.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.capstone_1.domain.BoardImage;
import com.example.capstone_1.domain.FreeBoard;
import com.example.capstone_1.dto.*;
import com.example.capstone_1.repository.FreeBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.amazonaws.services.s3.model.ObjectMetadata;


@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class FreeBoardServiceImpl implements FreeBoardService{

    private final ModelMapper modelMapper;
    private final FreeBoardRepository freeBoardRepository;

    private final UserService userService;

    private final AmazonS3 amazonS3; // AmazonS3 객체를 주입 받아야 합니다.
    private final String bucketName = "caspton-bucket"; // S3 버킷 이름을 여기에 설정하세요.

    @Override
    public Long register(BoardDTO boardDTO,String imagePath) {
        String loggedInUserEmail = userService.getLoggedInUserEmail();
        boardDTO.setWriter(loggedInUserEmail);
        FreeBoard board = dtoToEntityFreeBoard(boardDTO);

        if (boardDTO.getFileNames() != null) {
            for (String fileName : boardDTO.getFileNames()) {
                String[] arr = fileName.split("_");
                uploadImageToS3(arr[0], arr[1], imagePath);
            }
        }

        Long bno = freeBoardRepository.save(board).getBno();

        return bno;
    }

    private void uploadImageToS3(String uuid, String fileName, String imagePath) {
        try {
            InputStream inputStream = getImageInputStream(imagePath);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/jpeg");

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, uuid + "_" + fileName, inputStream, metadata);
            amazonS3.putObject(putObjectRequest);
        } catch (Exception e) {
            // 업로드 실패 시 예외 처리
        }
    }
    private InputStream getImageInputStream(String imagePath) throws FileNotFoundException {
        return new FileInputStream(imagePath);
    }
    @Override
    public BoardDTO readOne(Long bno) {

        //board_image까지 조인 처리되는 findByWithImages()를 이용
        Optional<FreeBoard> result = freeBoardRepository.findByIdWithImagesFreeBoard(bno);

        FreeBoard board = result.orElseThrow();

        BoardDTO boardDTO = entityToDTOFreeBoard(board);

        return boardDTO;
    }

    @Override
    public void modify(BoardDTO boardDTO, String imagePath) {

        Optional<FreeBoard> result = freeBoardRepository.findById(boardDTO.getBno());

        FreeBoard board = result.orElseThrow();

        board.changeFreeBoard(boardDTO.getTitle(), boardDTO.getContent());

        //첨부파일의 처리
        board.clearImagesFreeBoard();

        if (boardDTO.getFileNames() != null) {
            for (String fileName : boardDTO.getFileNames()) {
                String[] arr = fileName.split("_");
                board.addImageFreeBoard(arr[0], arr[1]);
                uploadImageToS3(arr[0], arr[1],imagePath); // 새 이미지 업로드
            }
        }

        freeBoardRepository.save(board);

    }

    @Override
    public void remove(Long bno) {

        freeBoardRepository.deleteById(bno);
        // S3에서 이미지 삭제
        FreeBoard board = freeBoardRepository.findById(bno).orElseThrow();
        for (BoardImage image : board.getImageSetFreeBoard()) {
            deleteImageFromS3(image.getUuid() + "_" + image.getFileName());
        }
    }

    private void deleteImageFromS3(String key) {
        try {
            amazonS3.deleteObject(bucketName, key);
        } catch (AmazonServiceException e) {
            // 삭제 실패 시 예외 처리
        }
    }
    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<FreeBoard> result = freeBoardRepository.searchAll(types, keyword, pageable);

        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board,BoardDTO.class)).collect(Collectors.toList());


        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();

    }

    @Override
    public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListReplyCountDTO> result = freeBoardRepository.searchWithReplyCount(types, keyword, pageable);

        return PageResponseDTO.<BoardListReplyCountDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListAllDTO> result = freeBoardRepository.searchWithAll(types, keyword, pageable);

        return PageResponseDTO.<BoardListAllDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }


}
