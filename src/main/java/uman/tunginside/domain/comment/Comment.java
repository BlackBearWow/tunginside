package uman.tunginside.domain.comment;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import uman.tunginside.domain.member.Member;
import uman.tunginside.domain.post.Post;
import uman.tunginside.exception.BadRequestException;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Entity
@Table(name = "comments")
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    @Length(max = 15)
    private String ip_addr;
    @Length(max = 20)
    private String password;
    @NotNull @Length(max = 500)
    private String content;
    @NotNull
    private LocalDateTime create_at;
    private LocalDateTime last_modified_at;
    @NotNull
    private Boolean deleted;
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment prev_comment;

    public void writeComment(CommentWriteForm commentWriteForm, Post post, Optional<Member> optionalMember, Comment prevComment, String ip_addr) {
        this.post = post;
        // 로그인이라면 멤버를 세팅하고 익명이라면 아이피와 비밀번호 넣는다.
        if(optionalMember.isPresent()) {
            this.member = optionalMember.get();
        }
        else {
            if(commentWriteForm.getPassword() == null || commentWriteForm.getPassword().isEmpty()) {
                throw new BadRequestException("익명으로 게시글 작성시 비밀번호는 필수입니다");
            }
            this.ip_addr = ip_addr;
            this.password = commentWriteForm.getPassword();
        }
        this.content = commentWriteForm.getContent();
        this.create_at = LocalDateTime.now();
        this.deleted = false;
        this.prev_comment = prevComment;
    }

    public void update(CommentUpdateDTO commentUpdateDTO, Optional<Member> optionalMember) {
        // 1. 댓글의 member가 null이 아니고 세션이 있어야하고 세션과 동일인경우.
        // 2. 댓글의 비밀번호가 있고 보낸 비밀번호가 동일한 경우.
        if( (this.member != null && optionalMember.isPresent() && this.member.getId().equals(optionalMember.get().getId()) ) ||
                (this.password != null && this.password.equals(commentUpdateDTO.getPassword())) ) {
            this.content = commentUpdateDTO.getContent();
            this.last_modified_at = LocalDateTime.now();
        }
        else {
            throw new BadRequestException("자신이 쓴 댓글이 아니거나 비밀번호가 틀립니다");
        }
    }

    public void setStatusDeleted() {
        this.content = " ";
        this.deleted = true;
    }
}
