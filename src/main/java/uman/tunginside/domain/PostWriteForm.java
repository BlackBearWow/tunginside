package uman.tunginside.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter @Setter
public class PostWriteForm {
    @NotNull @Length(max = 20)
    private String abbr;
    @NotNull @Length(max = 255)
    private String title;
    @NotNull @Length(max = 1000)
    private String content;
    private String password;
}
