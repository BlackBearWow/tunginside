package uman.tunginside.repository;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import uman.tunginside.domain.Post;
import uman.tunginside.domain.PostDetailDTO;
import uman.tunginside.domain.PostSummaryDTO;
import uman.tunginside.domain.PostUpdateForm;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    public void save(Post post);
    public void update(PostUpdateForm postUpdateForm, Long post_id, String ip_addr);
    public void remove(Long postId);
    public Optional<Post> findById(long id);
    public Optional<Post> findByAbbreviation(String abbreviation);
    List<PostSummaryDTO> findByConditions(String abbr, Integer page, Integer likeCut, String search);
    List<PostSummaryDTO> findByLikeCut(Integer likeCut);
    PostDetailDTO findDetailById(Long postId);
    void increaseLikeCount(Long postId);
}
