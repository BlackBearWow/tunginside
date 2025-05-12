package uman.tunginside.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter @Setter
public class CommentWriteForm {
    @Length(max = 20)
    private String password;
    @NotNull @Length(max = 500)
    private String content;
    private Long prev_comment_id;
}
