package org.zerock.b01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.BankBoard;
import org.zerock.b01.domain.FreeBoard;
import org.zerock.b01.dto.*;
import org.zerock.b01.repository.BankBoardRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BankBoardServiceImpl implements BankBoardService{

    private final ModelMapper modelMapper;
    private final BankBoardRepository bankBoardRepository;
    private final UserService userService;

    @Override
    public Long register(BankBoardDTO boardDTO) {

        String loggedInUserEmail = userService.getLoggedInUserEmail();
        boardDTO.setWriter(loggedInUserEmail);

        BankBoard board = dtoToEntityBankBoard(boardDTO);
        Long bno = bankBoardRepository.save(board).getBno();

        return bno;
    }

    @Override
    public BankBoardDTO readOne(Long bno) {
        //board_image까지 조인 처리되는 findByWithImages()를 이용
        Optional<BankBoard> result = bankBoardRepository.findByIdWithImagesBankBoard(bno);

        BankBoard board = result.orElseThrow();

        BankBoardDTO boardDTO = entityToDTOBankBoard(board);

        return boardDTO;
    }

    @Override
    public void modify(BankBoardDTO boardDTO) {
        Optional<BankBoard> result = bankBoardRepository.findById(boardDTO.getBno());

        BankBoard board = result.orElseThrow();

        board.changeBankBoard(boardDTO.getTitle(), boardDTO.getContent(), boardDTO.getAccountNumber(),boardDTO.getBankName(), boardDTO.getAccountHolder());//수정내용

        //첨부파일의 처리
        board.clearImagesBankBoard();

        if(boardDTO.getFileNames() != null){
            for (String fileName : boardDTO.getFileNames()) {
                String[] arr = fileName.split("_");
                board.addImageBankBoard(arr[0], arr[1]);
            }
        }

        bankBoardRepository.save(board);
    }

    @Override
    public void remove(Long bno) {
        bankBoardRepository.deleteById(bno);
    }

    @Override
    public PageResponseDTO<BankBoardDTO> list(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BankBoard> result = bankBoardRepository.searchAll(types, keyword, pageable);

        List<BankBoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board,BankBoardDTO.class)).collect(Collectors.toList());


        return PageResponseDTO.<BankBoardDTO>withAll()
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

        Page<BoardListReplyCountDTO> result = bankBoardRepository.searchWithReplyCount(types, keyword, pageable);

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

        Page<BoardListAllDTO> result = bankBoardRepository.searchWithAll(types, keyword, pageable);

        return PageResponseDTO.<BoardListAllDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }
}
