package uman.tunginside.repository;

import uman.tunginside.domain.comment.Comment;
import uman.tunginside.domain.comment.CommentDTO;
import uman.tunginside.domain.post.Post;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    List<CommentDTO> findByPost(Post post);
    Optional<Comment> findById(Long id);
    void save(Comment comment);
    Long findNestedCommentCount(Comment prevComment);
    void delete(Comment comment);
}
