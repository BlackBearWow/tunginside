package uman.tunginside.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uman.tunginside.domain.*;
import uman.tunginside.exception.BadRequestException;
import uman.tunginside.repository.CommentRepository;
import uman.tunginside.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public void writeComment(Long post_id, CommentWriteForm commentWriteForm, Member member, String ip_addr) {
        Comment comment = new Comment();
        comment.setPost(postRepository.findById(post_id)
                .orElseThrow(() -> new BadRequestException("없는 게시글입니다")));
        // 로그인이라면 멤버를 세팅하고 익명이라면 아이피와 비밀번호 넣는다.
        if(member != null) {
            comment.setMember(member);
        }
        else {
            comment.setIp_addr(ip_addr);
            if(commentWriteForm.getPassword() == null || commentWriteForm.getPassword().isEmpty()) {
                throw new BadRequestException("익명으로 게시글 작성시 비밀번호는 필수입니다");
            }
            comment.setPassword(commentWriteForm.getPassword());
        }
        comment.setContent(commentWriteForm.getContent());
        comment.setCreate_at(LocalDateTime.now());
        comment.setDeleted(false);
        if(commentWriteForm.getPrev_comment_id() != null) {
            Comment findComment = commentRepository.findById(commentWriteForm.getPrev_comment_id())
                    .orElseThrow(() -> new BadRequestException("이전 댓글을 찾을 수 없습니다"));
            comment.setPrev_comment(findComment);
        }
        commentRepository.save(comment);
    }

    public List<CommentDTO> getComments(Long post_id) {
        Post post = postRepository.findById(post_id).orElseThrow(() -> new BadRequestException("없는 게시글입니다"));
        return commentRepository.findByPost(post);
    }

    public void updateComment(CommentUpdateDTO commentUpdateDTO, Member member) {
        Comment comment = commentRepository.findById(commentUpdateDTO.getComment_id()).orElseThrow(() -> new BadRequestException("없는 댓글입니다"));
        // 1. 댓글의 member가 null이 아니고 세션이 있어야하고 세션과 동일인경우.
        // 2. 댓글의 비밀번호가 있고 보낸 비밀번호가 동일한 경우.
        if( (comment.getMember() != null && member != null && comment.getMember().getId().equals(member.getId()) ) ||
                (comment.getPassword() != null && comment.getPassword().equals(commentUpdateDTO.getPassword())) ) {
            commentRepository.update(commentUpdateDTO);
        }
        else {
            throw new BadRequestException("자신이 쓴 댓글이 아니거나 비밀번호가 틀립니다");
        }
    }

    public void deleteComment(CommentDeleteDTO commentDeleteDTO, Member member) {
        Comment comment = commentRepository.findById(commentDeleteDTO.getComment_id()).orElseThrow(() -> new BadRequestException("없는 댓글입니다"));
        // 1. 댓글의 member가 null이 아니고 세션이 있어야하고 세션과 동일인경우.
        // 2. 댓글의 비밀번호가 있고 보낸 비밀번호가 동일한 경우.
        if( (comment.getMember() != null && member != null && comment.getMember().getId().equals(member.getId()) ) ||
                (comment.getPassword() != null && comment.getPassword().equals(commentDeleteDTO.getPassword())) ) {
            // 대댓글이 있다면 삭제상태로 변경.
            if(commentRepository.findNestedCommentCount(comment) > 0) {
                commentRepository.updateToDeleted(comment);
            }
            // 없다면 삭제.
            else {
                Comment prevComment = comment.getPrev_comment();
                commentRepository.delete(comment);
                // 이전 댓글이 있었다면 이전 댓글 삭제상태를 조사해 삭제상태라면 nestedComment개수가 0이라면 이전댓글도 삭제.
                if(prevComment != null && prevComment.getDeleted() && (commentRepository.findNestedCommentCount(prevComment) == 0L)) {
                    commentRepository.delete(prevComment);
                }
            }
        }
        else {
            throw new BadRequestException("자신이 쓴 댓글이 아니거나 비밀번호가 틀립니다");
        }
    }
}
