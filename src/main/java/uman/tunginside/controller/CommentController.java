package uman.tunginside.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uman.tunginside.domain.comment.*;
import uman.tunginside.domain.member.MemberRole;
import uman.tunginside.security.MemberContext;
import uman.tunginside.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{post_id}")
    public String writeComment(@PathVariable Long post_id, @RequestBody @Validated CommentWriteForm commentWriteForm, @AuthenticationPrincipal MemberContext memberContext, HttpServletRequest request) {
        Long member_id = memberContext == null? null : memberContext.getId();
        commentService.writeComment(post_id, commentWriteForm, member_id, request.getRemoteAddr());
        return "댓글 작성 성공";
    }

    @GetMapping("/{post_id}")
    public List<CommentDTO> getComments(@PathVariable Long post_id) {
        List<Comment> comments = commentService.getComments(post_id);
        return comments.stream().map(CommentDTO::new).toList();
    }

    @PutMapping
    public String updateComment(@RequestBody @Validated CommentUpdateDTO commentUpdateDTO, @AuthenticationPrincipal MemberContext memberContext) {
        Long member_id = memberContext == null? null : memberContext.getId();
        commentService.updateComment(commentUpdateDTO, member_id);
        return "댓글 수정 성공";
    }

    @DeleteMapping
    public String deleteComment(@RequestBody @Validated CommentDeleteDTO commentDeleteDTO, @AuthenticationPrincipal MemberContext memberContext) {
        Long member_id = memberContext == null? null : memberContext.getId();
        MemberRole memberRole = memberContext == null? null: memberContext.getMemberRole();
        commentService.deleteComment(commentDeleteDTO, member_id, memberRole);
        return "댓글 삭제 성공";
    }
}
