package uman.tunginside.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uman.tunginside.domain.member.MemberRole;
import uman.tunginside.domain.post.*;
import uman.tunginside.security.MemberContext;
import uman.tunginside.service.PostService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    @PostMapping
    public String save(@RequestBody @Validated PostWriteForm postWriteForm, @AuthenticationPrincipal MemberContext memberContext, HttpServletRequest request) {
        Long member_id = memberContext == null? null : memberContext.getId();
        postService.writePost(postWriteForm, member_id, request.getRemoteAddr());
        return "글쓰기 성공";
    }

    @GetMapping
    public PostListDTO getPostListDTOByConditions(@Validated PostGetForm postGetForm) {
        return postService.getPostListDTOByConditions(postGetForm);
    }

    @GetMapping("/{post_id}")
    public PostDetailDTO getPostDetail(@PathVariable Long post_id) {
        return postService.getPostDetail(post_id);
    }

    @PutMapping("/{post_id}")
    public String updatePost(@RequestBody @Validated PostUpdateForm postUpdateForm, @PathVariable Long post_id, @AuthenticationPrincipal MemberContext memberContext, HttpServletRequest request) {
        Long member_id = memberContext == null? null : memberContext.getId();
        postService.updatePost(postUpdateForm, post_id, member_id, request.getRemoteAddr());
        return "수정 성공";
    }

    @DeleteMapping("/{post_id}")
    public String deletePost(@RequestBody Map<String, String> data, @PathVariable Long post_id, @AuthenticationPrincipal MemberContext memberContext) {
        String password = data.get("password");
        Long member_id = memberContext == null? null : memberContext.getId();
        MemberRole memberRole = memberContext == null? null: memberContext.getMemberRole();
        postService.deletePost(post_id, password, member_id, memberRole);
        return "삭제 성공";
    }

    @PostMapping("/{post_id}/like")
    public String likePost(@PathVariable Long post_id, @AuthenticationPrincipal MemberContext memberContext, HttpServletRequest request) {
        Long member_id = memberContext == null? null : memberContext.getId();
        postService.postLike(post_id, member_id, request.getRemoteAddr());
        return "좋아요 성공";
    }

    @PostMapping("/{post_id}/dislike")
    public String dislikePost(@PathVariable Long post_id, @AuthenticationPrincipal MemberContext memberContext, HttpServletRequest request) {
        Long member_id = memberContext == null? null : memberContext.getId();
        postService.postDislike(post_id, member_id, request.getRemoteAddr());
        return "싫어요 성공";
    }
}
