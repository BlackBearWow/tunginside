package uman.tunginside.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uman.tunginside.domain.post.*;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    public void save(Post post) {
        em.persist(post);
    }

    public void remove(Long postId) {
        Post post = em.find(Post.class, postId);
        em.remove(post);
    }

    public Optional<Post> findById(long id) {
        return Optional.ofNullable(em.find(Post.class, id));
    }

    public Optional<Post> findByAbbreviation(String abbreviation) {
        return em.createQuery("select p from Post p join p.category c on c.abbreviation =: abbreviation", Post.class)
                .setParameter("abbreviation", abbreviation).getResultList().stream().findFirst();
    }

    // querydsl로 짜면 좋지않을까나
    public List<PostSummaryDTO> findByConditions(String abbr, Integer page, Integer likeCut, String search) {
        String jpql = "select p.id, c.name, m.nickname, p.title, p.ip_addr, p.create_at, p.last_modified_at, p.post_like_count, p.comment_count from Post p join p.category c on c.abbreviation =: abbreviation left join p.member m";
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

    public Long countByConditions(String abbr, Integer page, Integer likeCut, String search) {
        String jpql = "select count(p) from Post p join p.category c on c.abbreviation =: abbreviation left join p.member m";
        if (likeCut != null && search != null) {
            jpql = jpql + " where p.post_like_count >= " + likeCut + " and (p.title like :search or p.content like :search)";
        }
        else if (likeCut != null) {
            jpql = jpql + " where p.post_like_count >= " + likeCut;
        }
        else if (search != null) {
            jpql = jpql + " where (p.title like :search or p.content like :search)";
        }
        TypedQuery<Long> result = em.createQuery(jpql, Long.class).setParameter("abbreviation", abbr);
        if (search != null) {
            result = result.setParameter("search", "%" + search + "%");
        }
        return result.getSingleResult();
    }

    public List<Post> findByLikeCut(Integer likeCut) {
        return em.createQuery("select p from Post p join fetch p.category c left join fetch p.member m where p.post_like_count >= :likeCut", Post.class)
                .setParameter("likeCut", likeCut).getResultList();
    }

    public Long countByLikeCut(Integer likeCut) {
        return em.createQuery("select count(p) from Post p join p.category c left join p.member m where p.post_like_count >= :likeCut", Long.class)
                .setParameter("likeCut", likeCut).getSingleResult();
    }

    public Optional<PostDetailDTO> findDetailById(Long postId) {
        return em.createQuery("select p.id, c.name, c.abbreviation, m.nickname, p.ip_addr, p.last_modified_ip, p.create_at, p.last_modified_at, p.title, p.content, p.post_like_count, p.post_dislike_count, p.comment_count from Post p join p.category c on p.id = :postId left join p.member m", PostDetailDTO.class)
                .setParameter("postId", postId).getResultList().stream().findFirst();
    }
}
