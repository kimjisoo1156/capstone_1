package com.example.capstone_1.service;

import com.example.capstone_1.domain.NoticeReply;
import com.example.capstone_1.dto.PageRequestDTO;
import com.example.capstone_1.dto.PageResponseDTO;
import com.example.capstone_1.dto.ReplyDTO;
import com.example.capstone_1.repository.NoticeReplyRepository;
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
public class NoticeReplyServiceImpl implements ReplyService{
    private final NoticeReplyRepository noticeReplyRepository;

    private final ModelMapper modelMapper;
    private final UserService userService;

    @Override
    public Long register(ReplyDTO replyDTO) {
        String loggedInUserEmail = userService.getLoggedInUserEmail();
        replyDTO.setReplyer(loggedInUserEmail);
        NoticeReply reply = modelMapper.map(replyDTO, NoticeReply.class);

        Long rno = noticeReplyRepository.save(reply).getRno();

        return rno;
    }

    @Override
    public ReplyDTO read(Long rno) {
        Optional<NoticeReply> replyOptional = noticeReplyRepository.findById(rno);

        NoticeReply reply = replyOptional.orElseThrow();

        return modelMapper.map(reply, ReplyDTO.class);
    }

    @Override
    public void modify(ReplyDTO replyDTO) {
        Optional<NoticeReply> replyOptional = noticeReplyRepository.findById(replyDTO.getRno());

        NoticeReply reply = replyOptional.orElseThrow();

        reply.changeTextNoticeReply(replyDTO.getReplyText());

        noticeReplyRepository.save(reply);
    }

    @Override
    public void remove(Long rno) {
        noticeReplyRepository.deleteById(rno);
    }

    @Override
    public PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() <=0? 0: pageRequestDTO.getPage() -1,
                pageRequestDTO.getSize(),
                Sort.by("rno").ascending());

        Page<NoticeReply> result = noticeReplyRepository.listOfBoardNoticeReply(bno, pageable);

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
        noticeReplyRepository.deleteByNoticeBoard_Bno(bno);

    }
}
