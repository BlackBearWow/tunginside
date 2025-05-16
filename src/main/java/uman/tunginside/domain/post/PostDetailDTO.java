package uman.tunginside.domain.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailDTO {
    private Long id;
    private String category_name;
    private String category_abbr;
    private String nickname;
    private String ip_addr;
    private String last_modified_ip;
    private LocalDateTime create_at;
    private LocalDateTime last_modified_at;
    private String title;
    private String content;
    private Integer post_like_count;
    private Integer post_dislike_count;
    private Integer comment_count;
}
