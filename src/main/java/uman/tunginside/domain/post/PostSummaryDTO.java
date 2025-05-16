package uman.tunginside.domain.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostSummaryDTO {
    private Long id;
    private String category_name;
    private String nickname;
    private String title;
    private String ip_addr;
    private LocalDateTime create_at;
    private LocalDateTime last_modified_at;
    private Integer post_like_count;
    private Integer comment_count;
}
