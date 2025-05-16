package uman.tunginside.domain.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Length;
import uman.tunginside.domain.category.Category;
import uman.tunginside.domain.member.Member;

import java.time.LocalDateTime;

@Getter @Setter
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
}
