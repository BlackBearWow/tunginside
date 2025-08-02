package uman.tunginside.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uman.tunginside.domain.comment.Comment;
import uman.tunginside.domain.post.Post;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    public List<Comment> findByPost(Post post);

    @Query("select count(c) from Comment c where c.prev_comment = :prevComment")
    public Long findNestedCommentCount(@Param("prevComment") Comment prevComment);
}
