package com.example.capstone_1.service;

import com.example.capstone_1.domain.FreeBoard;
import com.example.capstone_1.domain.ReportBoard;
import com.example.capstone_1.dto.*;
import com.example.capstone_1.repository.ReportBoardRepository;
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
    public void modify(BoardDTO boardDTO) {
        Optional<ReportBoard> result = reportBoardRepository.findById(boardDTO.getBno());

        ReportBoard board = result.orElseThrow();

        board.changeReportBoard(boardDTO.getTitle(), boardDTO.getContent());

        reportBoardRepository.save(board);
    }

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

        return PageResponseDTO.<BoardListReplyCountDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public ReportBoard findById(Long bno) {
        return reportBoardRepository.findById(bno).orElse(null);
    }


}
