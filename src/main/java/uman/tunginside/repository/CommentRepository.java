package uman.tunginside.repository;

import uman.tunginside.domain.Comment;

import java.util.Optional;

public interface CommentRepository {
    Optional<Comment> findById(Long id);
    void save(Comment comment);
}
