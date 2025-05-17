package uman.tunginside.domain.post;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostWriteForm {
    @NotNull @Length(max = 20, message = "줄임말은 최대 길이가 {max}입니다")
    private String abbr;
    @NotNull @Length(max = 255, message = "제목은 최대 길이가 {max}입니다")
    private String title;
    @NotNull @Length(max = 1000, message = "내용은 최대 길이가 {max}입니다")
    private String content;
    @Length(max = 20, message = "패스워드는 최대 길이가 {max}입니다")
    private String password;
}
