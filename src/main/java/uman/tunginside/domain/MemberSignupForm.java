package uman.tunginside.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter @Setter
public class MemberSignupForm {
    @NotBlank @Length(min=4, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9]+$",
            message = "아이디는 영문, 숫자만 가능합니다.")
    private String userid;
    @NotBlank @Length(min=4, max = 20)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]+$",
            message = "비밀번호는 영문, 숫자만 가능합니다. 영문, 숫자를 하나는 꼭 포함해야 합니다.")
    private String password;
    @NotBlank @Length(max = 20)
    private String nickname;
}
