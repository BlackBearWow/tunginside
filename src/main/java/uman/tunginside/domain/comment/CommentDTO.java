package uman.tunginside.domain.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String nickname;
    private String ip_addr;
    private String content;
    private LocalDateTime create_at;
    private LocalDateTime last_modified_at;
    private Boolean deleted;
    private Long prev_comment_id;
}
