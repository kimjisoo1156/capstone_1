package com.example.capstone_1.service;

import com.example.capstone_1.domain.FreeReply;
import com.example.capstone_1.dto.PageRequestDTO;
import com.example.capstone_1.dto.PageResponseDTO;
import com.example.capstone_1.dto.ReplyDTO;
import com.example.capstone_1.repository.FreeReplyRepository;
import com.example.capstone_1.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Log4j2
public class FreeReplyServiceImpl implements ReplyService, ReplyRepository {

    private final FreeReplyRepository freeReplyRepository;

    private final ModelMapper modelMapper;

    private final UserService userService;

    @Override
    public Long register(ReplyDTO replyDTO) {
        String loggedInUserEmail = userService.getLoggedInUserEmail();
        replyDTO.setReplyer(loggedInUserEmail);
        FreeReply reply = modelMapper.map(replyDTO, FreeReply.class);

        Long rno = freeReplyRepository.save(reply).getRno();

        return rno;
    }

    @Override
    public ReplyDTO read(Long rno) {

        Optional<FreeReply> replyOptional = freeReplyRepository.findById(rno);

        FreeReply reply = replyOptional.orElseThrow();

        return modelMapper.map(reply, ReplyDTO.class);
    }

    @Override
    public void modify(ReplyDTO replyDTO) {

        Optional<FreeReply> replyOptional = freeReplyRepository.findById(replyDTO.getRno());

        FreeReply reply = replyOptional.orElseThrow();

        reply.changeTextFreeReply(replyDTO.getReplyText());

        freeReplyRepository.save(reply);

    }

    @Override
    public void remove(Long rno) {

        freeReplyRepository.deleteById(rno);

    }

    @Override
    public PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() <=0? 0: pageRequestDTO.getPage() -1,
                pageRequestDTO.getSize(),
                Sort.by("rno").ascending());

        Page<FreeReply> result = freeReplyRepository.listOfBoardFreeReply(bno, pageable);

        List<ReplyDTO> dtoList =
                result.getContent().stream().map(reply -> modelMapper.map(reply, ReplyDTO.class))
                        .collect(Collectors.toList());

        return PageResponseDTO.<ReplyDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    @Transactional
    public void removeRepliesByBoardId(Long bno) {
        // 주어진 게시물 번호(bno)에 해당하는 모든 댓글들을 삭제합니다.
        freeReplyRepository.deleteByFreeBoard_Bno(bno);
    }


    @Override
    public String getWriterOfReply(Long rno) {
        FreeReply reply = freeReplyRepository.findById(rno).orElse(null);
        if (reply != null) {
            return reply.getReplyer();
        }else{
            return null;
        }
    }
}
