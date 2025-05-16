package uman.tunginside.repository;

import uman.tunginside.domain.member.Member;
import uman.tunginside.domain.post.Post;
import uman.tunginside.domain.post.PostLike;

import java.util.Optional;

public interface PostLikeRepository {
    public Optional<PostLike> findByPostAndMember(Post post, Member member);
    public Optional<PostLike> findByPostAndIp(Post post, String ip_addr);
    void save(PostLike postLike);
}
