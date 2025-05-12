package uman.tunginside.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uman.tunginside.domain.LoginForm;
import uman.tunginside.domain.Member;
import uman.tunginside.domain.MemberSignupForm;
import uman.tunginside.service.MemberService;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public String signup(@ModelAttribute @Validated MemberSignupForm memberSignupForm) {
        // 회원가입. 중복 id와 nickname은 service 계층에서 검증한다.
        memberService.signup(memberSignupForm);
        return "회원가입 성공";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute @Validated LoginForm loginForm, HttpSession session) {
        // 로그인. id와 password 검사는 service 계층에서 한다.
        Member result = memberService.login(loginForm);
        // 세션 정보 저장
        session.setAttribute("member", result);
        return "로그인 성공";
    }

    @GetMapping
    public Member getMember(@SessionAttribute(name = "member") Member member) {
        return member;
    }

    @PutMapping
    public String updateMember(@Validated MemberSignupForm memberSignupForm, @SessionAttribute(name = "member") Member member, HttpSession session) {
        memberService.update(memberSignupForm, member, session);
//        session.setAttribute("member", memberService.update(memberSignupForm, member));
        return "업데이트 성공";
    }

    @DeleteMapping
    public String deleteMember(@SessionAttribute(name = "member") Member member) {
        memberService.delete(member);
        return "탈퇴 성공";
    }
}
