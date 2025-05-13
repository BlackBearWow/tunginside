package uman.tunginside.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uman.tunginside.domain.*;
import uman.tunginside.service.PostService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    @PostMapping
    public String save(@RequestBody @Validated PostWriteForm postWriteForm, @SessionAttribute(value = "member", required = false) Member member, HttpServletRequest request) {
        postService.writePost(postWriteForm, member, request.getRemoteAddr());
        return "글쓰기 성공";
    }

    @GetMapping
    public List<PostSummaryDTO> getPostSummaryDTOsByConditions(@RequestBody @Validated PostGetForm postGetForm) {
        return postService.getPostSummaryDTOs(postGetForm);
    }

    @GetMapping("/best")
    public List<PostSummaryDTO> getBestPosts() {
        return postService.getBestPosts();
    }

    @GetMapping("/{post_id}")
    public PostDetailDTO getPostDetail(@PathVariable Long post_id) {
        return postService.getPostDetail(post_id);
    }

    @PutMapping("/{post_id}")
    public String updatePost(@RequestBody @Validated PostUpdateForm postUpdateForm, @PathVariable Long post_id, @SessionAttribute(value = "member", required = false) Member member, HttpServletRequest request) {
        postService.updatePost(postUpdateForm, post_id, member, request.getRemoteAddr());
        return "수정 성공";
    }

    @DeleteMapping("/{post_id}")
    public String deletePost(@RequestParam(required = false) String password, @PathVariable Long post_id, @SessionAttribute(value = "member", required = false) Member member) {
        postService.deletePost(post_id, password, member);
        return "삭제 성공";
    }

    @GetMapping("/{post_id}/like")
    public String likePost(@PathVariable Long post_id, @SessionAttribute(value = "member", required = false) Member member, HttpServletRequest request) {
        postService.postLike(post_id, member, request.getRemoteAddr());
        return "좋아요 성공";
    }

    @GetMapping("/{post_id}/dislike")
    public String dislikePost(@PathVariable Long post_id, @SessionAttribute(value = "member", required = false) Member member, HttpServletRequest request) {
        postService.postDislike(post_id, member, request.getRemoteAddr());
        return "싫어요 성공";
    }
}
