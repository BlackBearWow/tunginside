package uman.tunginside.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uman.tunginside.domain.Member;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional
public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em;

    @Override
    public void save(Member member) {
        em.persist(member);
    }

    @Override
    public void update(Long id, String userid, String password, String nickname) {
        Member member = em.find(Member.class, id);
        member.setUserid(userid);
        member.setPassword(password);
        member.setNickname(nickname);
    }

    @Override
    public Optional<Member> findById(long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    @Override
    public Optional<Member>  findByUserid(String userid) {
        return em.createQuery("select m from Member m where m.userid = :userid", Member.class)
                .setParameter("userid", userid).getResultList().stream().findFirst();
    }

    @Override
    public void delete(Member member) {
        em.remove(member);
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
