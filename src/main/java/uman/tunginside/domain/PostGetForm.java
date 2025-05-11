package uman.tunginside.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter @Setter
public class PostGetForm {
    @NotNull @Length(max = 20)
    private String abbr;
    @NotNull
    private Integer page;
    private Integer like_cut;
    private String search;
}
