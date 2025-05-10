package uman.tunginside.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uman.tunginside.domain.Member;
import uman.tunginside.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void signup(Member member) {
        // 중복 아이디 회원 검색
        // 중복 닉네임 회원 검색
        memberRepository.save(member);
    }

    public Member findById(long id) {
        return memberRepository.findById(id);
    }

    public void delete(Member member) {
        memberRepository.delete(member);
    }

}
