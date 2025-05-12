package uman.tunginside.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "post_like")
public class PostLike {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_like_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    @Length(max = 15)
    private String ip_addr;
    @NotNull
    private LocalDateTime created_at;
}
