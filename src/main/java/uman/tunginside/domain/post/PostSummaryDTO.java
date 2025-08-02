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
    private String abbr;
    private String nickname;
    private String title;
    private String ip_addr;
    private LocalDateTime create_at;
    private LocalDateTime last_modified_at;
    private Integer post_like_count;
    private Integer comment_count;
    private Integer view_count;

    public PostSummaryDTO(Post post) {
        this.id = post.getId();
        this.abbr = post.getCategory().getAbbr();
        if(post.getMember() != null) {
            this.nickname = post.getMember().getNickname();
        }
        this.title = post.getTitle();
        this.ip_addr = post.getIpAddr();
        this.create_at = post.getCreate_at();
        this.last_modified_at = post.getLast_modified_at();
        this.post_like_count = post.getPost_like_count();
        this.comment_count = post.getComment_count();
        this.view_count = post.getView_count();
    }
}
