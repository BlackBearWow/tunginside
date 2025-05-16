package uman.tunginside.repository;

import uman.tunginside.domain.post.*;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    public void save(Post post);
    public void remove(Long postId);
    public Optional<Post> findById(long id);
    public Optional<Post> findByAbbreviation(String abbreviation);
    List<PostSummaryDTO> findByConditions(String abbr, Integer page, Integer likeCut, String search);
    Long countByConditions(String abbr, Integer page, Integer likeCut, String search);
    List<PostSummaryDTO> findByLikeCut(Integer likeCut);
    Long countByLikeCut(Integer likeCut);
    Optional<PostDetailDTO> findDetailById(Long postId);
}
