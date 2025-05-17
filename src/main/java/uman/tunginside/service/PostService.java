package uman.tunginside.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uman.tunginside.domain.category.Category;
import uman.tunginside.domain.member.Member;
import uman.tunginside.domain.post.*;
import uman.tunginside.exception.BadRequestException;
import uman.tunginside.repository.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostDislikeRepository postDislikeRepository;

    @Transactional
    public Long writePost(PostWriteForm postWriteForm, Long member_id, String ip_addr) {
        Optional<Member> optionalMember = memberRepository.findById(member_id);
        Post post = new Post();
        Category category = categoryRepository.findByAbbreviation(postWriteForm.getAbbr())
                .orElseThrow(() -> new BadRequestException("없는 카테고리입니다"));
        // 로그인이라면 멤버를 세팅하고 익명이라면 아이피와 비밀번호 넣는다.
        post.writePost(postWriteForm, category, optionalMember, ip_addr);
        postRepository.save(post);
        return post.getId();
    }

    public PostListDTO getPostList(PostGetForm postGetForm) {
        PostListDTO postListDTO = new PostListDTO();
        postListDTO.setTotalCount(postRepository.countByConditions(postGetForm.getAbbr(), postGetForm.getPage(), postGetForm.getLike_cut(), postGetForm.getSearch()));
        postListDTO.setPosts(postRepository.findByConditions(postGetForm.getAbbr(), postGetForm.getPage(), postGetForm.getLike_cut(), postGetForm.getSearch()));
        return postListDTO;
    }

    public PostListDTO getBestPosts() {
        PostListDTO postListDTO = new PostListDTO();
        postListDTO.setTotalCount(postRepository.countByLikeCut(3));
        postListDTO.setPosts(postRepository.findByLikeCut(3));
        return postListDTO;
    }

    public PostDetailDTO getPostDetail(Long postId) {
        return postRepository.findDetailById(postId).orElseThrow(() -> new BadRequestException("없는 게시글입니다"));
    }

    @Transactional
    public void updatePost(PostUpdateForm postUpdateForm, Long postId, Long member_id, String ip_addr) {
        Optional<Member> optionalMember = memberRepository.findById(member_id);
        Post post = postRepository.findById(postId).orElseThrow(() -> new BadRequestException("없는 게시글입니다"));
        // 1. 게시글의 member가 null이 아니고 세션이 있어야하고 세션과 동일인경우.
        // 2. 게시글의 비밀번호가 있고 보낸 비밀번호가 동일한 경우.
        post.updatePost(postUpdateForm, optionalMember, ip_addr);
    }

    @Transactional
    public void deletePost(Long postId, String password, Long member_id) {
        Optional<Member> optionalMember = memberRepository.findById(member_id);
        Post post = postRepository.findById(postId).orElseThrow(() -> new BadRequestException("없는 게시글입니다"));
        // 1. 게시글의 member가 null이 아니고 세션이 있어야하고 세션과 동일인경우.
        // 2. 게시글의 비밀번호가 있고 보낸 비밀번호가 동일한 경우.
        if( (post.getMember() != null && optionalMember.isPresent() && post.getMember().getId().equals(optionalMember.get().getId()) ) ||
                (post.getPassword() != null && post.getPassword().equals(password)) ) {
            postRepository.remove(postId);
        }
        else {
            throw new BadRequestException("자신이 쓴 게시글이 아니거나 비밀번호가 틀립니다");
        }
    }

    @Transactional
    public void postLike(Long post_id, Long member_id, String ip_addr) {
        Optional<Member> optionalMember = memberRepository.findById(member_id);
        Post post = postRepository.findById(post_id).orElseThrow(() -> new BadRequestException("게시글이 존재하지 않습니다"));
        PostLike postLike = new PostLike();
        if(optionalMember.isPresent()) {
            postLikeRepository.findByPostAndMember(post, optionalMember.get())
                    .ifPresent((pl)-> {throw new BadRequestException("좋아요는 게시물 하나당 한번씩 가능합니다");});
            postLike.setMember(optionalMember.get());
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
        post.increasePostLikeCount();
    }

    @Transactional
    public void postDislike(Long post_id, Long member_id, String ip_addr) {
        Optional<Member> optionalMember = memberRepository.findById(member_id);
        Post post = postRepository.findById(post_id).orElseThrow(() -> new BadRequestException("게시글이 존재하지 않습니다"));
        PostDislike postDislike = new PostDislike();
        if(optionalMember.isPresent()) {
            postDislikeRepository.findByPostAndMember(post, optionalMember.get())
                    .ifPresent((pdl)->{throw new BadRequestException("싫어요는 게시물 하나당 한번씩 가능합니다");});
            postDislike.setMember(optionalMember.get());
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
        post.increasePostDislikeCount();
    }
}
