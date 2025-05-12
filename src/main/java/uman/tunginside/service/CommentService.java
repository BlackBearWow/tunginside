package uman.tunginside.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uman.tunginside.domain.Comment;
import uman.tunginside.domain.CommentWriteForm;
import uman.tunginside.domain.Member;
import uman.tunginside.repository.CommentRepository;
import uman.tunginside.repository.PostRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public void writeComment(Long post_id, CommentWriteForm commentWriteForm, Member member, String ip_addr) {
        Comment comment = new Comment();
        comment.setPost(postRepository.findById(post_id)
                .orElseThrow(() -> new RuntimeException("없는 게시글입니다")));
        // 로그인이라면 멤버를 세팅하고 익명이라면 아이피와 비밀번호 넣는다.
        if(member != null) {
            comment.setMember(member);
        }
        else {
            comment.setIp_addr(ip_addr);
            if(commentWriteForm.getPassword() == null || commentWriteForm.getPassword().isEmpty()) {
                throw new RuntimeException("익명으로 게시글 작성시 비밀번호는 필수입니다");
            }
            comment.setPassword(commentWriteForm.getPassword());
        }
        comment.setContent(commentWriteForm.getContent());
        comment.setCreate_at(LocalDateTime.now());
        if(commentWriteForm.getPrev_comment_id() != null) {
            Comment findComment = commentRepository.findById(commentWriteForm.getPrev_comment_id())
                    .orElseThrow(() -> new RuntimeException("이전 댓글을 찾을 수 없습니다"));
            comment.setPrev_comment(findComment);
        }
        commentRepository.save(comment);
    }
}
