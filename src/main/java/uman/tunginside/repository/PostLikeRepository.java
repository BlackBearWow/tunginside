package uman.tunginside.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uman.tunginside.domain.member.Member;
import uman.tunginside.domain.post.Post;
import uman.tunginside.domain.post.PostLike;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike,Long> {

    public Optional<PostLike> findByPostAndMember(Post post, Member member);

    public Optional<PostLike> findByPostAndIpAddr(Post post, String ip_addr);
}
