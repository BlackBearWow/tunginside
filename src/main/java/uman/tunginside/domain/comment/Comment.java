package uman.tunginside.domain.comment;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import uman.tunginside.domain.member.Member;
import uman.tunginside.domain.post.Post;

import java.time.LocalDateTime;

@Getter @Setter
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
}
