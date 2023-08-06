package com.example.capstone_1.Controller;

import com.example.capstone_1.Dto.ResponseDto;
import com.example.capstone_1.Dto.SignUpDto;
import com.example.capstone_1.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    @GetMapping("/")
    public String getBoard(@AuthenticationPrincipal String member_email) {
        return "로그인된 사용자는 " + member_email + "입니다.";
    }

}
