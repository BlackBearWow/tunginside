package uman.tunginside.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uman.tunginside.domain.Member;

@Repository
@RequiredArgsConstructor
public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em;

    @Override
    public void save(Member member) {
        em.persist(member);
    }

    @Override
    public Member findById(long id) {
        return em.find(Member.class, id);
    }

    @Override
    public void delete(Member member) {
        em.remove(member);
    }
}
