package uman.tunginside.domain.post;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter @Setter
public class PostUpdateForm {
    @NotNull
    @Length(max = 255, message = "제목은 최대 길이가 {max}입니다")
    private String title;
    @NotNull @Length(max = 1000, message = "내용은 최대 길이가 {max}입니다")
    private String content;
    @Length(max = 20, message = "패스워드는 최대 길이가 {max}입니다")
    private String password;
}
