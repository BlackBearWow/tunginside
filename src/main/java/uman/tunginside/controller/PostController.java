package uman.tunginside.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uman.tunginside.domain.member.Member;
import uman.tunginside.domain.post.*;
import uman.tunginside.service.PostService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    @PostMapping
    public String save(@RequestBody @Validated PostWriteForm postWriteForm, @SessionAttribute(required = false) Long member_id, HttpServletRequest request) {
        postService.writePost(postWriteForm, member_id, request.getRemoteAddr());
        return "글쓰기 성공";
    }

    @GetMapping
    public PostListDTO getPostListDTOsByConditions(@RequestBody @Validated PostGetForm postGetForm) {
        return postService.getPostList(postGetForm);
    }

    @GetMapping("/best")
    public PostListDTO getBestPosts() {
        return postService.getBestPosts();
    }

    @GetMapping("/{post_id}")
    public PostDetailDTO getPostDetail(@PathVariable Long post_id) {
        return postService.getPostDetail(post_id);
    }

    @PutMapping("/{post_id}")
    public String updatePost(@RequestBody @Validated PostUpdateForm postUpdateForm, @PathVariable Long post_id, @SessionAttribute(required = false) Long member_id, HttpServletRequest request) {
        postService.updatePost(postUpdateForm, post_id, member_id, request.getRemoteAddr());
        return "수정 성공";
    }

    @DeleteMapping("/{post_id}")
    public String deletePost(@RequestParam(required = false) String password, @PathVariable Long post_id, @SessionAttribute(required = false) Long member_id) {
        postService.deletePost(post_id, password, member_id);
        return "삭제 성공";
    }

    @PostMapping("/{post_id}/like")
    public String likePost(@PathVariable Long post_id, @SessionAttribute(required = false) Long member_id, HttpServletRequest request) {
        postService.postLike(post_id, member_id, request.getRemoteAddr());
        return "좋아요 성공";
    }

    @PostMapping("/{post_id}/dislike")
    public String dislikePost(@PathVariable Long post_id, @SessionAttribute(required = false) Long member_id, HttpServletRequest request) {
        postService.postDislike(post_id, member_id, request.getRemoteAddr());
        return "싫어요 성공";
    }
}
