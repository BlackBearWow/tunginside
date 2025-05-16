package uman.tunginside.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uman.tunginside.domain.Member;
import uman.tunginside.domain.Post;
import uman.tunginside.domain.PostLike;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaPostLikeRepository implements PostLikeRepository {

    private final EntityManager em;

    @Override
    public Optional<PostLike> findByPostAndMember(Post post, Member member) {
        return em.createQuery("select pl from PostLike pl where pl.post = :post and pl.member = :member", PostLike.class)
                .setParameter("post", post).setParameter("member", member).getResultList().stream().findFirst();
    }

    @Override
    public Optional<PostLike> findByPostAndIp(Post post, String ip_addr) {
        return em.createQuery("select pl from PostLike pl where pl.post = :post and pl.ip_addr = :ip_addr", PostLike.class)
                .setParameter("post", post).setParameter("ip_addr", ip_addr).getResultList().stream().findFirst();
    }

    @Override
    public void save(PostLike postLike) {
        em.persist(postLike);
    }
}
