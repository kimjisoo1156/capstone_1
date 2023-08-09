package com.example.capstone_1.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.capstone_1.domain.FreeBoard;
import com.example.capstone_1.dto.*;
import com.example.capstone_1.repository.FreeBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.model.ObjectMetadata;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class FreeBoardServiceImpl implements FreeBoardService{

    private final ModelMapper modelMapper;
    private final FreeBoardRepository freeBoardRepository;

    private final UserService userService;

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}") // S3 버킷 이름
    private String bucketName;
    @Override
    public Long register(BoardDTO boardDTO) {
        String loggedInUserEmail = userService.getLoggedInUserEmail();
        boardDTO.setWriter(loggedInUserEmail);
        FreeBoard board = dtoToEntityFreeBoard(boardDTO);

        Long bno = freeBoardRepository.save(board).getBno();

        return bno;
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
    public void modify(BoardDTO boardDTO, MultipartFile[] imageFiles) {
        Optional<FreeBoard> result = freeBoardRepository.findById(boardDTO.getBno());
        FreeBoard board = result.orElseThrow();

        board.changeFreeBoard(boardDTO.getTitle(), boardDTO.getContent());

        // Clear existing images
        board.clearImagesFreeBoard();

        // Upload new images and update URLs
        if (imageFiles != null && imageFiles.length > 0) {
            List<String> imageUrls = uploadImagesToS3(Arrays.asList(imageFiles));
            for (String imageUrl : imageUrls) {
                String uuid = UUID.randomUUID().toString();
                String fileName = extractFileNameFromUrl(imageUrl); // 이미지 URL에서 파일 이름 추출
                board.addImageFreeBoard(uuid, fileName, imageUrl);
            }
        }

        freeBoardRepository.save(board);
    }
    //이미지 URL에서 파일 이름 추출하는 메서드
    private String extractFileNameFromUrl(String imageUrl) {
        int lastSlashIndex = imageUrl.lastIndexOf("/");
        if (lastSlashIndex != -1) {
            return imageUrl.substring(lastSlashIndex + 1);
        }
        return null;
    }
    public List<String> uploadImagesToS3(List<MultipartFile> images) {
        List<String> imageUrls = new ArrayList<>();

        try {
            for (MultipartFile image : images) {
                String imageUrl = uploadImageToS3(image);
                imageUrls.add(imageUrl);
            }

            return imageUrls;
        } catch (Exception e) {
            log.error("Failed to upload images to S3: " + e.getMessage());
            return null;
        }
    }

    public String uploadImageToS3(MultipartFile image) {
        try {
            String uuid = UUID.randomUUID().toString();
            String s3FileName = uuid + "_" + image.getOriginalFilename();

            // 이미지 파일 업로드를 위한 InputStream 생성
            InputStream inputStream = image.getInputStream();

            // 업로드할 객체의 메타데이터 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(image.getSize()); // 파일 크기 설정
            metadata.setContentType(image.getContentType()); // 파일 타입 설정

            // S3에 업로드할 객체 생성
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3FileName, inputStream, metadata);

            // 이미지 파일 업로드
            PutObjectResult putObjectResult = amazonS3.putObject(putObjectRequest);

            // 업로드한 이미지의 URL 생성
            String imageUrl = amazonS3.getUrl(bucketName, s3FileName).toString();
            return imageUrl;
        } catch (Exception e) {
            log.error("Failed to upload image to S3: " + e.getMessage());
            return null;
        }
    }



    @Override
    public void remove(Long bno) {

        freeBoardRepository.deleteById(bno);

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
