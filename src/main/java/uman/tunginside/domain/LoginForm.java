package uman.tunginside.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter @Setter
public class LoginForm {
    @NotBlank @Length(min=4, max = 20)
    private String userid;
    @NotBlank @Length(min=4, max = 20)
    private String password;
}
