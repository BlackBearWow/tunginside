package uman.tunginside.repository;

import uman.tunginside.domain.Member;
import uman.tunginside.domain.MemberSignupForm;

import java.util.Optional;

public interface MemberRepository {
    void save(Member member);
    void update(Long id, String userid, String password, String nickname);
    Optional<Member> findById(long id);
    Optional<Member> findByUserid(String userid);
    void delete(Member member);
    boolean existsByUserid(String userid);
    boolean existsByNickname(String nickname);
}
