package uman.tunginside.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uman.tunginside.domain.member.Member;
import uman.tunginside.domain.post.Post;
import uman.tunginside.domain.post.PostDislike;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostDislikeRepository {

    private final EntityManager em;

    public Optional<PostDislike> findByPostAndMember(Post post, Member member) {
        return em.createQuery("select pdl from PostDislike pdl where pdl.post = :post and pdl.member = :member", PostDislike.class)
                .setParameter("post", post).setParameter("member", member).getResultList().stream().findFirst();
    }

    public Optional<PostDislike> findByPostAndIp(Post post, String ip_addr) {
        return em.createQuery("select pdl from PostDislike pdl where pdl.post = :post and pdl.ip_addr = :ip_addr", PostDislike.class)
                .setParameter("post", post).setParameter("ip_addr", ip_addr).getResultList().stream().findFirst();
    }

    public void save(PostDislike postDislike) {
        em.persist(postDislike);
    }
}
