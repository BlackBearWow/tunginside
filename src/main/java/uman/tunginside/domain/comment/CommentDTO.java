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

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        if(comment.getMember() != null) {
            this.nickname = comment.getMember().getNickname();
        }
        this.ip_addr = comment.getIp_addr();
        this.content = comment.getContent();
        this.create_at = comment.getCreate_at();
        this.last_modified_at = comment.getLast_modified_at();
        this.deleted = comment.getDeleted();
        if(comment.getPrev_comment() != null) {
            this.prev_comment_id = comment.getPrev_comment().getId();
        }
    }
}
