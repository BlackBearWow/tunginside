package uman.tunginside.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uman.tunginside.domain.comment.CommentDTO;
import uman.tunginside.domain.post.Post;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {

    private final EntityManager em;

    public List<CommentDTO> findCommentDTOsByPost(Post post) {
        return em.createQuery("select c.id, m.nickname, c.ip_addr, c.content, c.create_at, c.last_modified_at, c.deleted, c.prev_comment.id from Comment c left join c.member m where c.post = :post", CommentDTO.class)
                .setParameter("post", post).getResultList();
    }
}
