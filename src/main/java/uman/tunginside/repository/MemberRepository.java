package uman.tunginside.repository;

import uman.tunginside.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    void save(Member member);
    Optional<Member> findById(long id);
    Optional<Member> findByUserid(String userid);
    void delete(Member member);
    boolean existsByUserid(String userid);
    boolean existsByNickname(String nickname);
}
