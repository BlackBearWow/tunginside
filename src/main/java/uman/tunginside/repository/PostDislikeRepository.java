package uman.tunginside.repository;

import uman.tunginside.domain.member.Member;
import uman.tunginside.domain.post.Post;
import uman.tunginside.domain.post.PostDislike;

import java.util.Optional;

public interface PostDislikeRepository {
    public Optional<PostDislike> findByPostAndMember(Post post, Member member);
    public Optional<PostDislike> findByPostAndIp(Post post, String ip_addr);
    void save(PostDislike postDislike);
}
