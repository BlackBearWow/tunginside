package uman.tunginside.domain.post;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter @Setter
public class PostGetForm {
    @Length(max = 20)
    private String abbr;
    @NotNull
    private Integer page;
    private Integer like_cut;
    private String search;
    private Integer size = 20;
    @Enumerated(EnumType.STRING)
    private PostOrderby orderby;
}
