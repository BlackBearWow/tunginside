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
    public String save(@Validated PostWriteForm postWriteForm, @SessionAttribute(value = "member", required = false) Member member, HttpServletRequest request) {
        postService.writePost(postWriteForm, member, request.getRemoteAddr());
        return "글쓰기 성공";
    }

    @GetMapping
    public List<PostSummaryDTO> getPosts(@Validated PostGetForm postGetForm) {
        return postService.getPosts(postGetForm);
    }

    @GetMapping("/best")
    public List<PostSummaryDTO> getBestPosts() {
        return postService.getBestPosts();
    }
}
