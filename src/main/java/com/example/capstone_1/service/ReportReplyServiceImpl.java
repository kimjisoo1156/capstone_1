package org.zerock.b01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.FreeReply;
import org.zerock.b01.domain.Member;
import org.zerock.b01.domain.ReportReply;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.ReplyDTO;
import org.zerock.b01.repository.ReportReplyRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReportReplyServiceImpl implements ReplyService{

    private final ReportReplyRepository reportReplyRepository;

    private final ModelMapper modelMapper;
    private final UserService userService;

    @Override
    public Long register(ReplyDTO replyDTO) {
        String loggedInUserEmail = userService.getLoggedInUserEmail();
        replyDTO.setReplyer(loggedInUserEmail); //댓글 작성자를 로그인한 회원의 이메일로

        ReportReply reply = modelMapper.map(replyDTO, ReportReply.class);

        Long rno = reportReplyRepository.save(reply).getRno();

        return rno;
    }

    @Override
    public ReplyDTO read(Long rno) {
        Optional<ReportReply> replyOptional = reportReplyRepository.findById(rno);

        ReportReply reply = replyOptional.orElseThrow();

        return modelMapper.map(reply, ReplyDTO.class);
    }

    @Override
    public void modify(ReplyDTO replyDTO) {
        Optional<ReportReply> replyOptional = reportReplyRepository.findById(replyDTO.getRno());

        ReportReply reply = replyOptional.orElseThrow();

        reply.changeTextReportReply(replyDTO.getReplyText());

        reportReplyRepository.save(reply);
    }

    @Override
    public void remove(Long rno) {
        reportReplyRepository.deleteById(rno);
    }

    @Override
    public PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() <=0? 0: pageRequestDTO.getPage() -1,
                pageRequestDTO.getSize(),
                Sort.by("rno").ascending());

        Page<ReportReply> result = reportReplyRepository.listOfBoardReportReply(bno, pageable);

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
        reportReplyRepository.deleteByReportBoard_Bno(bno);
    }
}
