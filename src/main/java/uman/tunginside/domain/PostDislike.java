package uman.tunginside.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_dislike")
public class PostDislike {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_dislike_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    @Length(max = 10)
    private String ip_addr;
    @NotNull
    private LocalDateTime created_at;
}
