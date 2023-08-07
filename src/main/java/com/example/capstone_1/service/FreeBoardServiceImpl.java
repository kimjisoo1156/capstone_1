package org.zerock.b01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.FreeBoard;
import org.zerock.b01.dto.*;
import org.zerock.b01.repository.FreeBoardRepository;

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
    public void modify(BoardDTO boardDTO) {

        Optional<FreeBoard> result = freeBoardRepository.findById(boardDTO.getBno());

        FreeBoard board = result.orElseThrow();

        board.changeFreeBoard(boardDTO.getTitle(), boardDTO.getContent());

        //첨부파일의 처리
        board.clearImagesFreeBoard();

        if(boardDTO.getFileNames() != null){
            for (String fileName : boardDTO.getFileNames()) {
                String[] arr = fileName.split("_");
                board.addImageFreeBoard(arr[0], arr[1]);
            }
        }

        freeBoardRepository.save(board);

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
