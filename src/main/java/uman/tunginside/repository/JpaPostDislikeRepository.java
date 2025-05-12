package uman.tunginside.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uman.tunginside.domain.Member;
import uman.tunginside.domain.Post;
import uman.tunginside.domain.PostDislike;
import uman.tunginside.domain.PostLike;

import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class JpaPostDislikeRepository implements PostDislikeRepository {

    private final EntityManager em;

    @Override
    public Optional<PostDislike> findByPostAndMember(Post post, Member member) {
        return em.createQuery("select pdl from PostDislike pdl where pdl.post = :post and pdl.member = :member", PostDislike.class)
                .setParameter("post", post).setParameter("member", member).getResultList().stream().findFirst();
    }

    @Override
    public Optional<PostDislike> findByPostAndIp(Post post, String ip_addr) {
        return em.createQuery("select pdl from PostDislike pdl where pdl.post = :post and pdl.ip_addr = :ip_addr", PostDislike.class)
                .setParameter("post", post).setParameter("ip_addr", ip_addr).getResultList().stream().findFirst();
    }

    @Override
    public void save(PostDislike postDislike) {
        em.persist(postDislike);
    }
}
