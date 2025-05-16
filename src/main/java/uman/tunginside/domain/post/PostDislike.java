package uman.tunginside.domain.post;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import uman.tunginside.domain.member.Member;

import java.time.LocalDateTime;

@Getter @Setter
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
    @Length(max = 15)
    private String ip_addr;
    @NotNull
    private LocalDateTime created_at;
}
