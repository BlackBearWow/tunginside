package uman.tunginside.domain.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter @Setter
public class CategoryRegisterForm {
    @NotNull @Length(max = 60)
    private String name;
    @NotNull @Length(max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9-_]+$", message = "줄임말은 영문과 숫자와 -_만 가능합니다")
    private String abbreviation;
}
