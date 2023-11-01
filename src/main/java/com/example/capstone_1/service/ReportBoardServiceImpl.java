package com.example.capstone_1.service;

import com.example.capstone_1.domain.BoardType;
import com.example.capstone_1.domain.FileEntity;
import com.example.capstone_1.domain.FreeBoard;
import com.example.capstone_1.domain.ReportBoard;
import com.example.capstone_1.dto.*;
import com.example.capstone_1.repository.ReportBoardRepository;
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
public class ReportBoardServiceImpl implements ReportBoardService{

    private final ModelMapper modelMapper;
    private final ReportBoardRepository reportBoardRepository;
    private final UserService userService;
    private final FileService fileService;
    @Override
    public Long register(BoardDTO boardDTO) {
        String loggedInUserEmail = userService.getLoggedInUserEmail();
        boardDTO.setWriter(loggedInUserEmail); // 로그인한 회원의 이메일 값을 작성자로 설정

        ReportBoard board = dtoToEntityReportBoard(boardDTO);

        Long bno = reportBoardRepository.save(board).getBno();

        return bno;
    }

    @Override
    public BoardDTO readOne(Long bno) {
        //board_image까지 조인 처리되는 findByWithImages()를 이용
        Optional<ReportBoard> result = reportBoardRepository.findById(bno);

        ReportBoard board = result.orElseThrow();

        BoardDTO boardDTO = entityToDTOReportBoard(board);

        return boardDTO;
    }

    @Override
    public Board_File_DTO read(BoardType boardtype, Long bno) {
        // 게시물 조회
        Optional<ReportBoard> result = reportBoardRepository.findById(bno);
        ReportBoard board = result.orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + bno));
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
//        if (boardDTO.getBoardType() == BoardType.REPORT) {
//            Optional<ReportBoard> result = reportBoardRepository.findById(boardDTO.getBno());
//
//            ReportBoard board = result.orElseThrow();
//
//            board.changeReportBoard(boardDTO.getTitle(), boardDTO.getContent());
//
//            reportBoardRepository.save(board);
//        }
//
//    }

    @Override
    public void remove(Long bno) {
        reportBoardRepository.deleteById(bno);
    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<ReportBoard> result = reportBoardRepository.searchAll(types, keyword, pageable);

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

        Page<BoardListReplyCountDTO> result = reportBoardRepository.searchWithReplyCount(types, keyword, pageable);

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
                .total((int)result.getTotalElements())
                .build();
    }
    private String getSecretValue(Long bno) {
        // 예시로 Spring Data JPA를 사용하여 데이터베이스에서 secret 값을 가져오는 로직을 구현
        Optional<ReportBoard> result = reportBoardRepository.findById(bno); // yourRepository는 해당 엔티티의 레포지토리입니다.

        if (result.isPresent()) {
            ReportBoard entity = result.get();
            return entity.getSecret(); // 예시로 YourEntity 클래스의 getSecret() 메서드를 호출하여 secret 값을 가져옴
        } else {
            return ""; // 해당 bno에 해당하는 데이터가 없는 경우 빈 문자열을 반환하거나 적절한 방식으로 처리
        }
    }

    @Override
    public void modify(ReportBoard reportBoard, BoardDTO boardDTO) {
        reportBoard.changeReportBoard(boardDTO.getTitle(), boardDTO.getContent());
        reportBoardRepository.save(reportBoard);
    }

    @Override
    public ReportBoard findById(Long bno) {
        return reportBoardRepository.findById(bno).orElse(null);
    }


}
