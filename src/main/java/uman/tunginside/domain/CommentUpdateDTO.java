package uman.tunginside.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateDTO {
    @NotNull
    private Long comment_id;
    private String content;
    private String password;
}
