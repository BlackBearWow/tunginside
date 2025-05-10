package uman.tunginside.repository;

import uman.tunginside.domain.Member;

public interface MemberRepository {
    void save(Member member);
    Member findById(long id);
    void delete(Member member);
}
