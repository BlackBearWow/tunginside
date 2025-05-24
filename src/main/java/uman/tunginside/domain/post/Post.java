package uman.tunginside.domain.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Length;
import uman.tunginside.domain.category.Category;
import uman.tunginside.domain.comment.Comment;
import uman.tunginside.domain.member.Member;
import uman.tunginside.exception.BadRequestException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
@Entity
@Table(name = "posts")
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    @NotNull @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    @Length(max = 15)
    private String ip_addr;
    @Length(max = 15)
    private String last_modified_ip;
    private String password;
    @NotNull
    private LocalDateTime create_at;
    private LocalDateTime last_modified_at;
    @NotNull @Length(max = 255)
    private String title;
    @NotNull @Length(max = 1000)
    private String content;
    @NotNull @ColumnDefault("0")
    private Integer post_like_count;
    @NotNull @ColumnDefault("0")
    private Integer post_dislike_count;
    @NotNull @ColumnDefault("0")
    private Integer comment_count;
    @OneToMany(mappedBy = "post")
    private List<Comment> commentList;

    public void writePost(PostWriteForm postWriteForm, Category category, Optional<Member> optionalMember, String ip_addr) {
        this.category = category;
        // 로그인이라면 멤버를 세팅하고 익명이라면 아이피와 비밀번호 넣는다.
        if(optionalMember.isEmpty()) {
            if(postWriteForm.getPassword() == null || postWriteForm.getPassword().isBlank()) {
                throw new BadRequestException("익명으로 게시글 작성시 비밀번호는 필수입니다");
            }
            this.ip_addr = ip_addr;
            this.password = postWriteForm.getPassword();
        }
        else {
            this.member = optionalMember.get();
        }
        this.create_at = LocalDateTime.now();
        this.title = postWriteForm.getTitle();
        this.content = postWriteForm.getContent();
        this.post_like_count = 0;
        this.post_dislike_count = 0;
        this.comment_count = 0;
    }

    public void updatePost(PostUpdateForm postUpdateForm, Optional<Member> optionalMember, String ip_addr) {
        // 1. 게시글의 member가 null이 아니고 세션이 있어야하고 세션과 동일인경우.
        // 2. 게시글의 비밀번호가 있고 보낸 비밀번호가 동일한 경우.
        if( (this.member != null && optionalMember.isPresent() && this.member.getId().equals(optionalMember.get().getId()) ) ||
                (this.password != null && this.password.equals(postUpdateForm.getPassword())) ) {
            this.title = postUpdateForm.getTitle();
            this.content = postUpdateForm.getContent();
            this.last_modified_ip = ip_addr;
            this.last_modified_at = LocalDateTime.now();
        }
        else {
            throw new BadRequestException("자신이 쓴 게시글이 아니거나 비밀번호가 틀립니다");
        }
    }

    public void increasePostLikeCount() {
        this.post_like_count++;
    }

    public void increasePostDislikeCount() {
        this.post_dislike_count++;
    }
}
