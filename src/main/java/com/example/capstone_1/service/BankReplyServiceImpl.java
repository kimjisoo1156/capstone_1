//package com.example.capstone_1.service;
//
//import com.example.capstone_1.domain.BankReply;
//import com.example.capstone_1.dto.PageRequestDTO;
//import com.example.capstone_1.dto.PageResponseDTO;
//import com.example.capstone_1.dto.ReplyDTO;
//import com.example.capstone_1.repository.BankReplyRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.modelmapper.ModelMapper;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Log4j2
//public class BankReplyServiceImpl implements BankReplyService{
//    private final BankReplyRepository bankReplyRepository;
//
//    private final ModelMapper modelMapper;
//
//    private final UserService userService;
//
//    @Override
//    public Long register(ReplyDTO replyDTO) {
//        BankReply reply = modelMapper.map(replyDTO, BankReply.class);
//        String loggedInUserEmail = userService.getLoggedInUserEmail();
//        replyDTO.setReplyer(loggedInUserEmail);
//        Long rno = bankReplyRepository.save(reply).getRno();
//
//        return rno;
//    }
//
//    @Override
//    public ReplyDTO read(Long rno) {
//        Optional<BankReply> replyOptional = bankReplyRepository.findById(rno);
//
//        BankReply reply = replyOptional.orElseThrow();
//
//        return modelMapper.map(reply, ReplyDTO.class);
//    }
//
//    @Override
//    public void modify(ReplyDTO replyDTO) {
//        Optional<BankReply> replyOptional = bankReplyRepository.findById(replyDTO.getRno());
//
//        BankReply reply = replyOptional.orElseThrow();
//
//        reply.changeTextBankReply(replyDTO.getReplyText());
//
//        bankReplyRepository.save(reply);
//    }
//
//    @Override
//    public void remove(Long rno) {
//        bankReplyRepository.deleteById(rno);
//    }
//
//    @Override
//    public PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {
//        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() <=0? 0: pageRequestDTO.getPage() -1,
//                pageRequestDTO.getSize(),
//                Sort.by("rno").ascending());
//
//        Page<BankReply> result = bankReplyRepository.listOfBoardBankReply(bno, pageable);
//
//        List<ReplyDTO> dtoList =
//                result.getContent().stream().map(reply -> modelMapper.map(reply, ReplyDTO.class))
//                        .collect(Collectors.toList());
//
//        return PageResponseDTO.<ReplyDTO>withAll()
//                .pageRequestDTO(pageRequestDTO)
//                .dtoList(dtoList)
//                .total((int)result.getTotalElements())
//                .build();
//    }
//
//    @Override
//    @Transactional
//    public void removeRepliesByBoardId(Long bno) {
//
//        // 주어진 게시물 번호(bno)에 해당하는 모든 댓글들을 삭제합니다.
//        bankReplyRepository.deleteByBankBoard_Bno(bno);
//    }
//
//
//    @Override
//    public List<ReplyDTO> findByReplyText(String replyText) {
//
//        // BankReplyRepository의 findCommentByContent 메서드를 사용하여 댓글 조회
//        List<BankReply> replies = bankReplyRepository.findByReplyText(replyText);
//
//        // 댓글 엔티티를 DTO로 변환하여 반환
//        return replies.stream()
//                .map(reply -> modelMapper.map(reply, ReplyDTO.class))
//                .collect(Collectors.toList());
//    }
//}
