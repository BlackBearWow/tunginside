package uman.tunginside.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostSummaryDTO {
    private Long id;
    private String nickname;
    private String title;
    private String ip_addr;
    private Integer post_like_count;
    private Integer comment_count;
}
