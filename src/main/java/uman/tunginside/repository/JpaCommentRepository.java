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
public class JpaCommentRepository implements CommentRepository {

    private final EntityManager em;

    @Override
    public List<CommentDTO> findByPost(Post post) {
        return em.createQuery("select c.id, m.nickname, c.ip_addr, c.content, c.create_at, c.last_modified_at, c.deleted, c.prev_comment.id from Comment c left join c.member m where c.post = :post", CommentDTO.class)
                .setParameter("post", post).getResultList();
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Override
    public void save(Comment comment) {
        em.persist(comment);
    }

    @Override
    public Long findNestedCommentCount(Comment prevComment) {
        return em.createQuery("select count(c) from Comment c where c.prev_comment = :prevComment", Long.class)
                .setParameter("prevComment", prevComment).getSingleResult();
    }

    @Override
    public void delete(Comment comment) {
        em.remove(comment);
    }
}
