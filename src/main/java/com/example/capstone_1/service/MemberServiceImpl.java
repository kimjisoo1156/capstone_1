package com.example.capstone_1.service;

import com.example.capstone_1.domain.*;
import com.example.capstone_1.dto.DetailedMemberResponseDto;
import com.example.capstone_1.dto.MemberRequestDto;
import com.example.capstone_1.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService{

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FreeBoardRepository freeBoardRepository;

    @Autowired
    private NoticeBoardRepository noticeBoardRepository;

    @Autowired
    private ReportBoardRepository reportBoardRepository;


    @Autowired
    private FreeReplyRepository freeReplyRepository;

    @Autowired
    private NoticeReplyRepository noticeReplyRepository;

    @Autowired
    private ReportReplyRepository reportReplyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String getLoggedInUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    @Transactional
    public void updatePassword(String email, String newPassword) {
        // 이메일을 기반으로 회원을 찾아 새로운 비밀번호로 업데이트합니다.
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일을 찾을 수 없습니다."));

        if (!isValidPassword(newPassword)) {
            throw new IllegalArgumentException("Invalid password");
        }
        String encodedPassword = passwordEncoder.encode(newPassword);
        member.setPassword(encodedPassword);

        memberRepository.save(member);
    }
    private boolean isValidPassword(String password) {
        // 비밀번호 유효성 검사 로직
        return password.matches("^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$");
       // return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()-_+=]).{8,}$");
    }
    @Override
    public DetailedMemberResponseDto getMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일을 찾을 수 없습니다."));
        return DetailedMemberResponseDto.of(member);
    }

    @Transactional
    public void updateMember(String email, MemberRequestDto memberRequestDto) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일을 찾을 수 없습니다."));


        // 비밀번호 변경
        if (memberRequestDto.getPassword() != null) {
            String encodedPassword = passwordEncoder.encode(memberRequestDto.getPassword());
            member.setPassword(encodedPassword);
        }
        member.setPhoneNumber(memberRequestDto.getPhoneNumber());
        member.setName(memberRequestDto.getName());

        memberRepository.save(member);
    }

    @Transactional
    public void deleteMemberByEmail(String email) {

        // 회원을 찾아옴
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일을 찾을 수 없습니다."));

        // 작성한 게시물을 찾아서 작성자를 알 수 없음으로 변경
        List<FreeBoard> boards_Free = freeBoardRepository.findByWriter(email);
        for (FreeBoard board : boards_Free) {
            board.setWriterToUnknown();
        }
        List<NoticeBoard> boards_Notice = noticeBoardRepository.findByWriter(email);
        for (NoticeBoard board : boards_Notice) {
            board.setWriterToUnknown();
        }
        List<ReportBoard> boards_Report = reportBoardRepository.findByWriter(email);
        for (ReportBoard board : boards_Report) {
            board.setWriterToUnknown();
        }

        // 작성한 댓글을 찾아서 작성자를 알 수 없음으로 변경
        List<FreeReply> replies_Free = freeReplyRepository.findByReplyer(email);
        for (FreeReply reply : replies_Free) {
            reply.setReplyerToUnknown();
        }
        List<NoticeReply> replies_Notice = noticeReplyRepository.findByReplyer(email);
        for (NoticeReply reply : replies_Notice) {
            reply.setReplyerToUnknown();
        }
        List<ReportReply> replies_Report = reportReplyRepository.findByReplyer(email);
        for (ReportReply reply : replies_Report) {
            reply.setReplyerToUnknown();
        }

        // 변경된 게시물과 댓글을 저장
        freeBoardRepository.saveAll(boards_Free);
        freeReplyRepository.saveAll(replies_Free);

        noticeBoardRepository.saveAll(boards_Notice);
        noticeReplyRepository.saveAll(replies_Notice);

        reportBoardRepository.saveAll(boards_Report);
        reportReplyRepository.saveAll(replies_Report);

        // 회원을 삭제
        memberRepository.delete(member);

    }


}
