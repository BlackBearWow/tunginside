package uman.tunginside.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uman.tunginside.domain.*;
import uman.tunginside.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{post_id}")
    public String writeComment(@PathVariable Long post_id, @RequestBody @Validated CommentWriteForm commentWriteForm, @SessionAttribute(value = "member", required = false) Member member, HttpServletRequest request) {
        commentService.writeComment(post_id, commentWriteForm, member, request.getRemoteAddr());
        return "댓글 작성 성공";
    }

    @GetMapping("/{post_id}")
    public List<CommentDTO> getComments(@PathVariable Long post_id) {
        return commentService.getComments(post_id);
    }

    @PutMapping
    public String updateComment(@RequestBody @Validated CommentUpdateDTO commentUpdateDTO, @SessionAttribute(value = "member", required = false) Member member) {
        commentService.updateComment(commentUpdateDTO, member);
        return "댓글 수정 성공";
    }

    @DeleteMapping
    public String deleteComment(@RequestBody @Validated CommentDeleteDTO commentDeleteDTO, @SessionAttribute(value = "member", required = false) Member member) {
        commentService.deleteComment(commentDeleteDTO, member);
        return "댓글 삭제 성공";
    }
}
