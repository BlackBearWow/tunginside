package uman.tunginside.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uman.tunginside.domain.LoginForm;
import uman.tunginside.domain.Member;
import uman.tunginside.exception.DuplicateNicknameException;
import uman.tunginside.exception.DuplicateUseridException;
import uman.tunginside.exception.LoginFailException;
import uman.tunginside.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void signup(Member member) {
        // 중복 아이디 회원 검색
        if(memberRepository.existsByUserid(member.getUserid())) {
            throw new DuplicateUseridException("이미 사용중인 아이디입니다");
        }
        System.out.println("------------------------");
        // 중복 닉네임 회원 검색
        if(memberRepository.existsByNickname(member.getNickname())) {
            throw new DuplicateNicknameException("이미 사용중인 닉네임입니다");
        }
        // 중복이 없다면 세이브
        memberRepository.save(member);
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
