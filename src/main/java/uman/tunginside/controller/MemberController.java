package uman.tunginside.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uman.tunginside.domain.member.MemberLoginForm;
import uman.tunginside.domain.member.MemberResponseDTO;
import uman.tunginside.domain.member.MemberSignupForm;
import uman.tunginside.domain.member.MemberUpdateForm;
import uman.tunginside.security.MemberContext;
import uman.tunginside.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public String signup(@RequestBody @Validated MemberSignupForm memberSignupForm) {
        // 회원가입. 중복 id와 nickname은 service 계층에서 검증한다.
        memberService.signup(memberSignupForm);
        return "회원가입 성공";
    }

//    @PostMapping("/login")
//    public MemberResponseDTO login(@RequestBody @Validated MemberLoginForm memberLoginForm, HttpSession session) {
//        // 로그인. id와 password 검사는 service 계층에서 한다.
//        Long member_id = memberService.login(memberLoginForm);
//        // 세션 정보 저장
//        session.setAttribute("member_id", member_id);
//        return new MemberResponseDTO(memberService.getMember(member_id));
//    }

    @GetMapping
    public MemberResponseDTO getMemberInfo(@AuthenticationPrincipal User user) {
        return new MemberResponseDTO(memberService.getMemberByUserId(user.getUsername()));
    }

//    @PostMapping("/logout")
//    public String logout(HttpSession session) {
//        session.removeAttribute("member_id");
//        return "로그아웃";
//    }

    @PutMapping
    public MemberResponseDTO updateMember(@RequestBody @Validated MemberUpdateForm memberUpdateForm, @AuthenticationPrincipal MemberContext memberContext) {
        memberService.update(memberUpdateForm, memberContext.getId());
        return new MemberResponseDTO(memberService.getMember(memberContext.getId()));
    }

    @DeleteMapping
    public String deleteMember(@AuthenticationPrincipal MemberContext memberContext) {
        memberService.delete(memberContext.getId());
        return "탈퇴 성공";
    }
}
