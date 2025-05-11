package uman.tunginside.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uman.tunginside.domain.*;
import uman.tunginside.repository.CategoryRepository;
import uman.tunginside.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    public void writePost(PostWriteForm postWriteForm, Member member, String ip_addr) {
        Post post = new Post();
        post.setCategory(categoryRepository.findByAbbreviation(postWriteForm.getAbbr())
                .orElseThrow(()->new RuntimeException("없는 카테고리입니다")));
        // 로그인이라면 멤버를 세팅하고 익명이라면 아이피와 비밀번호 넣는다.
        if(member != null) {
            post.setMember(member);
        }
        else {
            post.setIp_addr(ip_addr);
            if(postWriteForm.getPassword() == null || postWriteForm.getPassword().isEmpty()) {
                throw new RuntimeException("익명으로 게시글 작성시 비밀번호는 필수입니다");
            }
            post.setPassword(postWriteForm.getPassword());
        }
        post.setCreate_at(LocalDateTime.now());
        post.setTitle(postWriteForm.getTitle());
        post.setContent(postWriteForm.getContent());
        post.setPost_like_count(0);
        post.setPost_dislike_count(0);
        post.setComment_count(0);
        postRepository.save(post);
    }

    public List<PostSummaryDTO> getPosts(PostGetForm postGetForm) {
        return postRepository.findByConditions(postGetForm.getAbbr(), postGetForm.getPage(), postGetForm.getLike_cut(), postGetForm.getSearch());
    }

    public List<PostSummaryDTO> getBestPosts() {
        return postRepository.findByLikeCut(3);
    }
}
