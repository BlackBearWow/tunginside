package uman.tunginside.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    @Length(max = 10)
    private String ip_addr;
    @Length(max = 20)
    private String password;
    @NotNull
    private LocalDateTime create_at;
    @NotNull
    private LocalDateTime last_modified_at;
    @NotNull @Length(max = 255)
    private String title;
    @NotNull @Length(max = 1000)
    private String content;
    @ColumnDefault("0")
    private Integer post_like_count;
    @ColumnDefault("0")
    private Integer post_dislike_count;
    @ColumnDefault("0")
    private Integer comment_count;
}
