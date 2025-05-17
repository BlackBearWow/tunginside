package uman.tunginside.domain.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter @Setter
public class MemberLoginForm {
    @NotBlank @Length(min=4, max = 20, message = "아이디 길이가 {min}이상 {max}이하여야 합니다")
    private String userid;
    @NotBlank @Length(min=4, max = 20, message = "비밀번호 길이가 {min}이상 {max}이하여야 합니다")
    private String password;
}
