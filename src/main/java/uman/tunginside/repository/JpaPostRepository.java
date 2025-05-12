package uman.tunginside.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uman.tunginside.domain.Post;
import uman.tunginside.domain.PostDetailDTO;
import uman.tunginside.domain.PostSummaryDTO;
import uman.tunginside.domain.PostUpdateForm;

import java.time.LocalDateTime;
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
    public void update(PostUpdateForm postUpdateForm, Long post_id, String ip_addr) {
        Post post = em.find(Post.class, post_id);
        post.setTitle(postUpdateForm.getTitle());
        post.setContent(postUpdateForm.getContent());
        post.setLast_modified_ip(ip_addr);
        post.setLast_modified_at(LocalDateTime.now());
    }

    @Override
    public void remove(Long postId) {
        Post post = em.find(Post.class, postId);
        em.remove(post);
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

    @Override
    public PostDetailDTO findDetailById(Long postId) {
        return em.createQuery("select p.id, c.name, c.abbreviation, m.nickname, p.ip_addr, p.last_modified_ip, p.create_at, p.last_modified_at, p.title, p.content, p.post_like_count, p.post_dislike_count, p.comment_count from Post p join p.category c on p.id = :postId left join p.member m", PostDetailDTO.class)
                .setParameter("postId", postId).getSingleResult();
    }

    @Override
    public void increaseLikeCount(Long postId) {
        Post post = em.find(Post.class, postId);
        post.setPost_like_count(post.getPost_like_count() + 1);
    }
}
