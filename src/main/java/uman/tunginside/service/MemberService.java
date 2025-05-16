package uman.tunginside.service;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uman.tunginside.domain.LoginForm;
import uman.tunginside.domain.Member;
import uman.tunginside.domain.MemberSignupForm;
import uman.tunginside.exception.DuplicateNicknameException;
import uman.tunginside.exception.DuplicateUseridException;
import uman.tunginside.exception.LoginFailException;
import uman.tunginside.repository.MemberRepository;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void signup(MemberSignupForm memberSignupForm) {
        // 중복 아이디 회원 검색
        if(memberRepository.existsByUserid(memberSignupForm.getUserid())) {
            throw new DuplicateUseridException("이미 사용중인 아이디입니다");
        }
        // 중복 닉네임 회원 검색
        if(memberRepository.existsByNickname(memberSignupForm.getNickname())) {
            throw new DuplicateNicknameException("이미 사용중인 닉네임입니다");
        }
        // 중복이 없다면 세이브
        Member member = new Member();
        member.setUserid(memberSignupForm.getUserid());
        member.setPassword(memberSignupForm.getPassword());
        member.setNickname(memberSignupForm.getNickname());
        member.setCreate_at(LocalDateTime.now());
        memberRepository.save(member);
    }

    public void update(MemberSignupForm memberSignupForm, Member member, HttpSession session) {
        // 중복 아이디 회원 검색. 자신의 아이디는 중복 검사하지 않는다.
        if(!memberSignupForm.getUserid().equals(member.getUserid())) {
            if(memberRepository.existsByUserid(memberSignupForm.getUserid())) {
                throw new DuplicateUseridException("이미 사용중인 아이디입니다");
            }
        }
        // 중복 닉네임 회원 검색. 자신의 닉네임은 중복 검사하지 않는다.
        if(!memberSignupForm.getNickname().equals(member.getNickname())) {
            if(memberRepository.existsByNickname(memberSignupForm.getNickname())) {
                throw new DuplicateNicknameException("이미 사용중인 닉네임입니다");
            }
        }
        // 중복이 없다면 업데이트
        memberRepository.update(member.getId(), memberSignupForm.getUserid(), memberSignupForm.getPassword(), memberSignupForm.getNickname());
        session.setAttribute("member", memberRepository.findById(member.getId()).get());
    }

    public Member login(LoginForm loginForm) {
        Member member = memberRepository.findByUserid(loginForm.getUserid())
                .orElseThrow(() -> new LoginFailException("존재하지 않는 멤버입니다."));
        if (member.getPassword().equals(loginForm.getPassword())) {
            return member;
        }
        else
            throw new LoginFailException("비밀번호가 일치하지 않습니다");
    }

    public void delete(Member member) {
        memberRepository.delete(member);
    }

}
