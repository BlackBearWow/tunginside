package uman.tunginside.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uman.tunginside.domain.Member;
import uman.tunginside.domain.MemberSignupForm;
import uman.tunginside.service.MemberService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public String signup(@ModelAttribute @Validated MemberSignupForm memberSignupForm) {
        // 중복 id 검사
        // 중복 username 검사
        return "회원가입 성공";
    }
}
