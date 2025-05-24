package uman.tunginside.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uman.tunginside.domain.post.PostSummaryDTO;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {

    private final EntityManager em;

    public List<PostSummaryDTO> findPostSummaryDTOByLikeCut(Integer likeCut) {
        return em.createQuery("select p.id, c.name, m.nickname, p.title, p.ip_addr, p.create_at, p.last_modified_at, p.post_like_count, p.comment_count from Post p join p.category c left join p.member m where p.post_like_count >= :likeCut", PostSummaryDTO.class)
                .setParameter("likeCut", likeCut).getResultList();
    }
}
