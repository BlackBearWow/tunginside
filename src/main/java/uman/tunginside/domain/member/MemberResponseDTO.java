package uman.tunginside.domain.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
public class MemberResponseDTO {
    private String userid;
    private String nickname;
    private LocalDateTime create_at;
}
