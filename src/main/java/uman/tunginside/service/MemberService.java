package uman.tunginside.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uman.tunginside.domain.member.MemberLoginForm;
import uman.tunginside.domain.member.Member;
import uman.tunginside.domain.member.MemberSignupForm;
import uman.tunginside.domain.member.MemberUpdateForm;
import uman.tunginside.exception.*;
import uman.tunginside.repository.MemberRepository;

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
    public void update(MemberUpdateForm muf, Long member_id, HttpSession session) {
        Member member = memberRepository.findById(member_id).orElseThrow(() -> new BadRequestException("없는 멤버입니다"));
        // 현재 패스워드가 다르다면 에러
        if(!member.getPassword().equals(muf.getPassword())) {
            throw new PasswordException("비밀번호가 일치하지 않습니다");
        }
        // userid가 null이거나 빈칸이지 않고 자기자신의 아이디가 아니라면, 중복검사
        if(muf.getUserid() != null && !muf.getUserid().isBlank() && !muf.getUserid().equals(member.getUserid())) {
            if(memberRepository.existsByUserid(muf.getUserid())) {
                throw new UseridException("이미 사용중인 아이디입니다");
            }
        }
        // nickname이 null이거나 빈칸이지 않고 자기자신의 닉네임이 아니라면, 중복검사
        if(muf.getNickname() != null && !muf.getNickname().isBlank() && !muf.getNickname().equals(member.getNickname())) {
            if(memberRepository.existsByNickname(muf.getNickname())) {
                throw new NicknameException("이미 사용중인 닉네임입니다");
            }
        }
        // newPassword가 null이거나 빈칸이지 않고 패턴을 만족하지 않으면 에러
        if(muf.getNewPassword() != null && !muf.getNewPassword().isBlank() && !muf.getNewPassword().matches("^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{4,20}$")) {
            throw new NewPasswordException("비밀번호는 영문, 숫자만 가능합니다. 영문, 숫자를 하나는 꼭 포함해야 합니다. 4~20자리여야 합니다");
        }
        // 중복이 없다면 업데이트
        member.update(muf);
    }

    @Transactional
    public void delete(Long member_id) {
        Member member = memberRepository.findById(member_id).orElseThrow(() -> new BadRequestException("멤버가 없습니다"));
        memberRepository.delete(member);
    }
}
