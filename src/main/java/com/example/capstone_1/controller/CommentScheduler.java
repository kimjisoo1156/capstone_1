package com.example.capstone_1.controller;

import com.example.capstone_1.domain.BankReply;
import com.example.capstone_1.repository.BankReplyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@EnableScheduling
public class CommentScheduler extends TextWebSocketHandler {   //웹소켓이 지원이 안되면, api로 주고 받자..하지만 실시간에 적합 x??

    private Set<WebSocketSession> sessions = new HashSet<>();
    private BankReplyRepository bankReplyRepository;

    public CommentScheduler(BankReplyRepository bankReplyRepository) {
        this.bankReplyRepository = bankReplyRepository;
    }
    @Scheduled(fixedDelay = 60000)
    public void sendDownloadActivationMessage(){
        String message = "download active";

        List<BankReply> replies = bankReplyRepository.findByReplyText("확인했습니다.");
        if (!replies.isEmpty()) {
            BankReply bankReply = replies.get(0);
            if (bankReply != null && isAdmin(bankReply.getReplyer())){
                //댓글 작성자를 기준으로 관리자
                for (WebSocketSession session : sessions) {
                    try {
                        session.sendMessage(new TextMessage(message));
                    } catch (IOException e) {
                        // 에러 처리
                    }
                }
            }
        }
    }
    // 사용자가 관리자인지 여부를 확인하는 메서드
    private boolean isAdmin(String replyer) {
        // 여기에 관리자인지 여부를 확인하는 로직을 구현합니다.
        // 예를 들어, 특정 사용자가 관리자 권한을 가지고 있는지를 확인하는 코드를 작성할 수 있습니다.
        // 실제 구현은 데이터베이스나 외부 인증 시스템과 연동하여 확인하는 것이 일반적입니다.
        // 이 예제에서는 단순히 "admin"이라는 사용자 이름을 관리자로 간주하겠습니다.

        return "admin".equals(replyer);
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    @GetMapping("/downloadActivation") //관리자 권한도 추가하는거 넣어야함.
    public ResponseEntity<String> checkDownloadActivation() {
        List<BankReply> replies = bankReplyRepository.findByReplyText("확인했습니다.");
        if (!replies.isEmpty()) {
            return ResponseEntity.ok("download active");
        } else {
            return ResponseEntity.ok("download inactive");
        }
    }

}
