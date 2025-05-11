package uman.tunginside.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uman.tunginside.domain.Post;
import uman.tunginside.domain.PostSummaryDTO;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional
public class JpaPostRepository implements PostRepository {

    private final EntityManager em;

    @Override
    public void save(Post post) {
        em.persist(post);
    }

    @Override
    public Optional<Post> findById(long id) {
        return Optional.ofNullable(em.find(Post.class, id));
    }

    @Override
    public Optional<Post> findByAbbreviation(String abbreviation) {
        return em.createQuery("select p from Post p join p.category c on c.abbreviation =: abbreviation", Post.class)
                .setParameter("abbreviation", abbreviation).getResultList().stream().findFirst();
    }

    // querydsl로 짜면 좋지않을까나
    @Override
    public List<PostSummaryDTO> findByConditions(String abbr, Integer page, Integer likeCut, String search) {
        String jpql = "select p.id, m.nickname, p.title, p.ip_addr, p.post_like_count, p.comment_count from Post p join p.category c on c.abbreviation =: abbreviation left join p.member m";
        if (likeCut != null && search != null) {
            jpql = jpql + " where p.post_like_count >= " + likeCut + " and (p.title like :search or p.content like :search)";
        }
        else if (likeCut != null) {
            jpql = jpql + " where p.post_like_count >= " + likeCut;
        }
        else if (search != null) {
            jpql = jpql + " where (p.title like :search or p.content like :search)";
        }
        TypedQuery<PostSummaryDTO> result = em.createQuery(jpql, PostSummaryDTO.class).setParameter("abbreviation", abbr);
        if (search != null) {
            result = result.setParameter("search", "%" + search + "%");
        }
        return result.setFirstResult((page - 1) * 20).setMaxResults(20).getResultList();
    }

    @Override
    public List<PostSummaryDTO> findByLikeCut(Integer likeCut) {
        return em.createQuery("select p.id, m.nickname, p.title, p.ip_addr, p.post_like_count, p.comment_count from Post p left join p.member m where p.post_like_count >= :likeCut", PostSummaryDTO.class)
                .setParameter("likeCut", likeCut).getResultList();
    }
}
