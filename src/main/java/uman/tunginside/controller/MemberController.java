package uman.tunginside.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uman.tunginside.domain.LoginForm;
import uman.tunginside.domain.Member;
import uman.tunginside.domain.MemberSignupForm;
import uman.tunginside.service.MemberService;

import java.time.LocalDateTime;
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
        Member member = new Member();
        member.setUserid(memberSignupForm.getUserid());
        member.setPassword(memberSignupForm.getPassword());
        member.setNickname(memberSignupForm.getNickname());
        member.setCreate_at(LocalDateTime.now());
        // 회원가입. 중복 id와 nickname은 service 계층에서 검증한다.
        memberService.signup(member);
        return "회원가입 성공";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute @Validated LoginForm loginForm) {
        // 로그인. id와 password 검사는 service 계층에서 한다.
        Member result = memberService.login(loginForm);
        return "로그인 성공";
    }
}
