package uman.tunginside.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uman.tunginside.domain.member.Member;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em;

    @Override
    public void save(Member member) {
        em.persist(member);
    }

    @Override
    public Optional<Member> findById(Long id) {
//        return Optional.ofNullable(em.find(Member.class, id));
        return em.createQuery("select m from Member m where m.id = :id", Member.class)
                .setParameter("id", id)
                .getResultList().stream().findFirst();
    }

    @Override
    public Optional<Member>  findByUserid(String userid) {
        return em.createQuery("select m from Member m where m.userid = :userid", Member.class)
                .setParameter("userid", userid).getResultList().stream().findFirst();
    }

    @Override
    public void delete(Long member_id) {
        em.remove(em.find(Member.class, member_id));
    }

    @Override
    public boolean existsByUserid(String userid) {
        Long count = em.createQuery("select count(m) from Member m where m.userid = :userid", Long.class)
                .setParameter("userid", userid).getSingleResult();
        return (count > 0);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        Long count = em.createQuery("select count(m) from Member m where m.nickname = :nickname", Long.class)
                .setParameter("nickname", nickname).getSingleResult();
        return (count > 0);
    }
}
