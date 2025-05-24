package uman.tunginside.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uman.tunginside.domain.comment.Comment;
import uman.tunginside.domain.comment.CommentDTO;
import uman.tunginside.domain.post.Post;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final EntityManager em;

    public List<Comment> findByPost(Post post) {
        return em.createQuery("select c from Comment c left join fetch c.member m where c.post = :post", Comment.class)
                .setParameter("post", post).getResultList();
    }

    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    public void save(Comment comment) {
        em.persist(comment);
    }

    public Long findNestedCommentCount(Comment prevComment) {
        return em.createQuery("select count(c) from Comment c where c.prev_comment = :prevComment", Long.class)
                .setParameter("prevComment", prevComment).getSingleResult();
    }

    public void delete(Comment comment) {
        em.remove(comment);
    }
}
