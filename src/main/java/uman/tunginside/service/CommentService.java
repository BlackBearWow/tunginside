package uman.tunginside.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uman.tunginside.domain.comment.*;
import uman.tunginside.domain.member.Member;
import uman.tunginside.domain.member.MemberRole;
import uman.tunginside.domain.post.Post;
import uman.tunginside.exception.BadRequestException;
import uman.tunginside.repository.CommentQueryRepository;
import uman.tunginside.repository.CommentRepository;
import uman.tunginside.repository.MemberRepository;
import uman.tunginside.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentQueryRepository commentQueryRepository;

    @Transactional
    public Long writeComment(Long post_id, CommentWriteForm commentWriteForm, Long member_id, String ip_addr) {
        Post post = postRepository.findById(post_id)
                .orElseThrow(() -> new BadRequestException("없는 게시글입니다"));
        Optional<Member> optionalMember = memberRepository.findByIdNullable(member_id);
        Comment prevComment = null;
        if(commentWriteForm.getPrev_comment_id() != null) {
            prevComment = commentRepository.findById(commentWriteForm.getPrev_comment_id())
                    .orElseThrow(() -> new BadRequestException("이전 댓글을 찾을 수 없습니다"));
            if(!prevComment.getPost().getId().equals(post.getId())) {
                throw new BadRequestException("같은 게시글의 댓글에만 대댓글을 달 수 있습니다");
            }
        }
        Comment comment = new Comment();
        comment.writeComment(commentWriteForm, post, optionalMember, prevComment, ip_addr);
        commentRepository.save(comment);
        post.increaseCommentCount();
        return comment.getId();
    }

    public List<Comment> getComments(Long post_id) {
        Post post = postRepository.findById(post_id).orElseThrow(() -> new BadRequestException("없는 게시글입니다"));
        return commentRepository.findByPost(post);
    }

    public List<CommentDTO> getCommentDTOs(Long post_id) {
        Post post = postRepository.findById(post_id).orElseThrow(() -> new BadRequestException("없는 게시글입니다"));
        return commentQueryRepository.findCommentDTOsByPost(post);
    }

    @Transactional
    public void updateComment(CommentUpdateDTO commentUpdateDTO, Long member_id) {
        Optional<Member> optionalMember = memberRepository.findByIdNullable(member_id);
        Comment comment = commentRepository.findById(commentUpdateDTO.getComment_id()).orElseThrow(() -> new BadRequestException("없는 댓글입니다"));
        comment.update(commentUpdateDTO, optionalMember);
    }

    @Transactional
    public void deleteComment(CommentDeleteDTO commentDeleteDTO, Long member_id, MemberRole memberRole) {
        Optional<Member> optionalMember = memberRepository.findByIdNullable(member_id);
        Comment comment = commentRepository.findById(commentDeleteDTO.getComment_id()).orElseThrow(() -> new BadRequestException("없는 댓글입니다"));
        Post post = comment.getPost();
        // 1. 댓글의 member가 null이 아니고 세션이 있어야하고 세션과 동일인경우.
        // 2. 댓글의 비밀번호가 있고 보낸 비밀번호가 동일한 경우.
        // 3. 관리자일 경우
        if( (comment.getMember() != null && optionalMember.isPresent() && comment.getMember().getId().equals(optionalMember.get().getId()) ) ||
                (comment.getPassword() != null && comment.getPassword().equals(commentDeleteDTO.getPassword())) || (memberRole == MemberRole.ADMIN)) {
            // 대댓글이 있다면 삭제상태로 변경.
            if(commentRepository.findNestedCommentCount(comment) > 0) {
                comment.setStatusDeleted();
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
            post.decreaseCommentCount();
        }
        else {
            throw new BadRequestException("자신이 쓴 댓글이 아니거나 비밀번호가 틀립니다");
        }
    }
}
