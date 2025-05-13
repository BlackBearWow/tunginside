package uman.tunginside.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uman.tunginside.domain.*;
import uman.tunginside.exception.BadRequestException;
import uman.tunginside.repository.CategoryRepository;
import uman.tunginside.repository.PostDislikeRepository;
import uman.tunginside.repository.PostLikeRepository;
import uman.tunginside.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostDislikeRepository postDislikeRepository;

    public void writePost(PostWriteForm postWriteForm, Member member, String ip_addr) {
        Post post = new Post();
        post.setCategory(categoryRepository.findByAbbreviation(postWriteForm.getAbbr())
                .orElseThrow(()->new BadRequestException("없는 카테고리입니다")));
        // 로그인이라면 멤버를 세팅하고 익명이라면 아이피와 비밀번호 넣는다.
        if(member != null) {
            post.setMember(member);
        }
        else {
            post.setIp_addr(ip_addr);
            if(postWriteForm.getPassword() == null || postWriteForm.getPassword().isEmpty()) {
                throw new BadRequestException("익명으로 게시글 작성시 비밀번호는 필수입니다");
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

    public List<PostSummaryDTO> getPostSummaryDTOs(PostGetForm postGetForm) {
        return postRepository.findByConditions(postGetForm.getAbbr(), postGetForm.getPage(), postGetForm.getLike_cut(), postGetForm.getSearch());
    }

    public List<PostSummaryDTO> getBestPosts() {
        return postRepository.findByLikeCut(3);
    }

    public PostDetailDTO getPostDetail(Long postId) {
        return postRepository.findDetailById(postId).orElseThrow(() -> new BadRequestException("없는 게시글입니다"));
    }

    public void updatePost(PostUpdateForm postUpdateForm, Long postId, Member member, String ip_addr) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new BadRequestException("없는 게시글입니다"));
        // 1. 게시글의 member가 null이 아니고 세션이 있어야하고 세션과 동일인경우.
        // 2. 게시글의 비밀번호가 있고 보낸 비밀번호가 동일한 경우.
        if( (post.getMember() != null && member != null && post.getMember().getId().equals(member.getId()) ) ||
                (post.getPassword() != null && post.getPassword().equals(postUpdateForm.getPassword())) ) {
            postRepository.update(postUpdateForm, postId, ip_addr);
        }
        else {
            throw new BadRequestException("자신이 쓴 게시글이 아니거나 비밀번호가 틀립니다");
        }
    }

    public void deletePost(Long postId, String password, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new BadRequestException("없는 게시글입니다"));
        // 1. 게시글의 member가 null이 아니고 세션이 있어야하고 세션과 동일인경우.
        // 2. 게시글의 비밀번호가 있고 보낸 비밀번호가 동일한 경우.
        if( (post.getMember() != null && member != null && post.getMember().getId().equals(member.getId()) ) ||
                (post.getPassword() != null && post.getPassword().equals(password)) ) {
            postRepository.remove(postId);
        }
        else {
            throw new BadRequestException("자신이 쓴 게시글이 아니거나 비밀번호가 틀립니다");
        }
    }

    public void postLike(Long post_id, Member member, String ip_addr) {
        Post post = postRepository.findById(post_id).orElseThrow(() -> new BadRequestException("게시글이 존재하지 않습니다"));
        PostLike postLike = new PostLike();
        if(member != null) {
            postLikeRepository.findByPostAndMember(post, member)
                    .ifPresent((pl)-> {throw new BadRequestException("좋아요는 게시물 하나당 한번씩 가능합니다");});
            postLike.setMember(member);
        }
        else {
            postLikeRepository.findByPostAndIp(post, ip_addr)
                    .ifPresent((pl)-> {throw new BadRequestException("좋아요는 게시물 하나당 한번씩 가능합니다");});
            postLike.setIp_addr(ip_addr);
        }
        postLike.setPost(post);
        postLike.setCreated_at(LocalDateTime.now());
        postLikeRepository.save(postLike);
        // 좋아요 개수 늘리기
        postRepository.increaseLikeCount(post_id);
    }

    public void postDislike(Long post_id, Member member, String ip_addr) {
        Post post = postRepository.findById(post_id).orElseThrow(() -> new BadRequestException("게시글이 존재하지 않습니다"));
        PostDislike postDislike = new PostDislike();
        if(member != null) {
            postDislikeRepository.findByPostAndMember(post, member)
                    .ifPresent((pdl)->{throw new BadRequestException("싫어요는 게시물 하나당 한번씩 가능합니다");});
            postDislike.setMember(member);
        }
        else {
            postDislikeRepository.findByPostAndIp(post, ip_addr)
                    .ifPresent((pdl)->{throw new BadRequestException("싫어요는 게시물 하나당 한번씩 가능합니다");});
            postDislike.setIp_addr(ip_addr);
        }
        postDislike.setPost(post);
        postDislike.setCreated_at(LocalDateTime.now());
        postDislikeRepository.save(postDislike);
        // 싫어요 개수 늘리기
        postRepository.increaseDislikeCount(post_id);
    }
}
