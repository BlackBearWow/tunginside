package uman.tunginside.domain.comment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateDTO {
    @NotNull
    private Long comment_id;
    @NotNull @Length(max = 500, message = "내용은 최대 길이가 {max}입니다")
    private String content;
    @Length(max = 20, message = "패스워드는 최대 길이가 {max}입니다")
    private String password;
}
