package uman.tunginside.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uman.tunginside.domain.Comment;
import uman.tunginside.domain.CommentWriteForm;
import uman.tunginside.domain.Member;
import uman.tunginside.service.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{post_id}")
    public String writeComment(@PathVariable Long post_id, @RequestBody @Validated CommentWriteForm commentWriteForm, @SessionAttribute(value = "member", required = false) Member member, HttpServletRequest request) {
        commentService.writeComment(post_id, commentWriteForm, member, request.getRemoteAddr());
        return "댓글작성 성공";
    }
}
