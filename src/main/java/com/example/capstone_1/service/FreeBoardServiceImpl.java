package com.example.capstone_1.service;

import com.example.capstone_1.domain.BoardType;
import com.example.capstone_1.domain.FileEntity;
import com.example.capstone_1.domain.FreeBoard;
import com.example.capstone_1.dto.*;
import com.example.capstone_1.repository.FreeBoardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class FreeBoardServiceImpl implements FreeBoardService{

    private final ModelMapper modelMapper;
    private final FreeBoardRepository freeBoardRepository;

    private final UserService userService;
    private final FileService fileService;

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

        Optional<FreeBoard> result = freeBoardRepository.findById(bno);

        FreeBoard board = result.orElseThrow();

        BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);

        return boardDTO;
    }

    @Override
    public Board_File_DTO read(BoardType boardtype, Long bno) { //게시물이 지금 자유 이미지만 되는거지..
        // 게시물 조회
        Optional<FreeBoard> result = freeBoardRepository.findById(bno);
        FreeBoard board = result.orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + bno));
        Board_File_DTO boardWithImages = modelMapper.map(board, Board_File_DTO.class);

        // 이미지 조회
        List<FileEntity> imageEntities = fileService.getImagesForBoard(boardtype, bno);

        // 이미지 데이터 추가
        List<String> imageUrls = imageEntities.stream().map(FileEntity::getS3Url).collect(Collectors.toList());
        boardWithImages.setFileNames(imageUrls);

        return boardWithImages;
    }



//    @Override
//    public void modify(BoardDTO boardDTO) {
//        if (boardDTO.getBoardType() == BoardType.FREE) {
//            Optional<FreeBoard> result = freeBoardRepository.findById(boardDTO.getBno());
//
//            FreeBoard board = result.orElseThrow();
//
//            board.changeFreeBoard(boardDTO.getTitle(), boardDTO.getContent());
//
//            freeBoardRepository.save(board);
//        }
//    }

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

        // 가져온 DTO 목록에 secret 값을 설정
        List<BoardListReplyCountDTO> dtos = result.getContent();
        for (BoardListReplyCountDTO dto : dtos) {
            // 데이터베이스에서 secret 값을 가져오는 메서드를 호출하여 설정
            String secret = getSecretValue(dto.getBno());
            dto.setSecret(secret);
        }

        return PageResponseDTO.<BoardListReplyCountDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int) result.getTotalElements())
                .build();
    }
    private String getSecretValue(Long bno) {
        // 예시로 Spring Data JPA를 사용하여 데이터베이스에서 secret 값을 가져오는 로직을 구현
        Optional<FreeBoard> result = freeBoardRepository.findById(bno); // yourRepository는 해당 엔티티의 레포지토리입니다.

        if (result.isPresent()) {
            FreeBoard entity = result.get();
            return entity.getSecret(); // 예시로 YourEntity 클래스의 getSecret() 메서드를 호출하여 secret 값을 가져옴
        } else {
            return ""; // 해당 bno에 해당하는 데이터가 없는 경우 빈 문자열을 반환하거나 적절한 방식으로 처리
        }
    }

    @Override
    public FreeBoard findById(Long bno) {
        return freeBoardRepository.findById(bno).orElse(null);
    }

    @Override
    public void modify(FreeBoard freeBoard, BoardDTO boardDTO) {
        freeBoard.changeFreeBoard(boardDTO.getTitle(), boardDTO.getContent());
        freeBoardRepository.save(freeBoard);
    }

}
