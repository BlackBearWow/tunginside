package uman.tunginside.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uman.tunginside.domain.member.MemberLoginForm;
import uman.tunginside.domain.member.Member;
import uman.tunginside.domain.member.MemberSignupForm;
import uman.tunginside.exception.BadRequestException;
import uman.tunginside.exception.NicknameException;
import uman.tunginside.exception.UseridException;
import uman.tunginside.exception.PasswordException;
import uman.tunginside.repository.MemberRepository;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long signup(MemberSignupForm memberSignupForm) {
        // 중복 아이디 회원 검색
        if(memberRepository.existsByUserid(memberSignupForm.getUserid())) {
            throw new UseridException("이미 사용중인 아이디입니다");
        }
        // 중복 닉네임 회원 검색
        if(memberRepository.existsByNickname(memberSignupForm.getNickname())) {
            throw new NicknameException("이미 사용중인 닉네임입니다");
        }
        // 중복이 없다면 세이브
        Member member = new Member();
        member.setFromMemberSignupForm(memberSignupForm);
        memberRepository.save(member);
        return member.getId();
    }

    public Long login(MemberLoginForm memberLoginForm) {
        Member member = memberRepository.findByUserid(memberLoginForm.getUserid())
                .orElseThrow(() -> new UseridException("존재하지 않는 멤버입니다."));
        if (member.getPassword().equals(memberLoginForm.getPassword())) {
            return member.getId();
        }
        else
            throw new PasswordException("비밀번호가 일치하지 않습니다");
    }

    public Member getMember(Long id) {
        return memberRepository.findById(id).orElseThrow(()->new BadRequestException("없는 멤버입니다"));
    }

    @Transactional
    public void update(MemberSignupForm memberSignupForm, Long member_id, HttpSession session) {
        Member member = memberRepository.findById(member_id).orElseThrow(() -> new BadRequestException("없는 멤버입니다"));
        // 중복 아이디 회원 검색. 자신의 아이디는 중복 검사하지 않는다.
        if(!memberSignupForm.getUserid().equals(member.getUserid())) {
            if(memberRepository.existsByUserid(memberSignupForm.getUserid())) {
                throw new UseridException("이미 사용중인 아이디입니다");
            }
        }
        // 중복 닉네임 회원 검색. 자신의 닉네임은 중복 검사하지 않는다.
        if(!memberSignupForm.getNickname().equals(member.getNickname())) {
            if(memberRepository.existsByNickname(memberSignupForm.getNickname())) {
                throw new NicknameException("이미 사용중인 닉네임입니다");
            }
        }
        // 중복이 없다면 업데이트
        member.update(memberSignupForm);
    }

    @Transactional
    public void delete(Long member_id) {
        memberRepository.delete(member_id);
    }

}
