package uman.tunginside.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

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
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comment;
}
