package uman.tunginside.repository;

import uman.tunginside.domain.Comment;
import uman.tunginside.domain.CommentDTO;
import uman.tunginside.domain.CommentUpdateDTO;
import uman.tunginside.domain.Post;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    List<CommentDTO> findByPost(Post post);
    Optional<Comment> findById(Long id);
    void save(Comment comment);
    void update(CommentUpdateDTO commentUpdateDTO);
    void updateToDeleted(Comment comment);
    Long findNestedCommentCount(Comment prevComment);
    void delete(Comment comment);
}
