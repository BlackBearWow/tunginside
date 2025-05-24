package uman.tunginside.domain.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateForm {
    private String userid;
    private String newPassword;
    private String nickname;
    @Length(min=4, max = 20, message = "비밀번호 길이가 {min}이상 {max}이하여야 합니다")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]+$",
            message = "비밀번호는 영문, 숫자만 가능합니다. 영문, 숫자를 하나는 꼭 포함해야 합니다.")
    private String password;
}
