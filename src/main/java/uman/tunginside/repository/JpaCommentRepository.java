package uman.tunginside.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uman.tunginside.domain.Comment;
import uman.tunginside.domain.CommentDTO;
import uman.tunginside.domain.CommentUpdateDTO;
import uman.tunginside.domain.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
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
    public void update(CommentUpdateDTO commentUpdateDTO) {
        Comment comment = em.find(Comment.class, commentUpdateDTO.getComment_id());
        comment.setContent(commentUpdateDTO.getContent());
        comment.setLast_modified_at(LocalDateTime.now());
    }

    @Override
    public void updateToDeleted(Comment comment) {
        Comment result = em.find(Comment.class, comment.getId());
        result.setContent(" ");
        result.setDeleted(true);
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
