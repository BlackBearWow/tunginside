package uman.tunginside.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uman.tunginside.domain.member.Member;
import uman.tunginside.domain.post.Post;
import uman.tunginside.domain.post.PostDislike;

import java.util.Optional;

public interface PostDislikeRepository extends JpaRepository<PostDislike,Long> {

    public Optional<PostDislike> findByPostAndMember(Post post, Member member);

    public Optional<PostDislike> findByPostAndIpAddr(Post post, String ipAddr);
}
