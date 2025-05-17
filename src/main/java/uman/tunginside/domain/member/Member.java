package uman.tunginside.domain.member;

import jakarta.persistence.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "member",
        uniqueConstraints =  {
        @UniqueConstraint(columnNames = {"userid"}),
        @UniqueConstraint(columnNames = {"nickname"})
})
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @NotNull @Length(max = 20)
    private String userid;
    @NotNull @Length(max = 20)
    private String password;
    @NotNull @Length(max = 20)
    private String nickname;
    @NotNull
    private LocalDateTime create_at;

    public MemberResponseDTO toMemberResponseDTO() {
        return new MemberResponseDTO(this.userid, this.nickname, this.create_at);
    }
    public void setFromMemberSignupForm(MemberSignupForm memberSignupForm) {
        this.userid = memberSignupForm.getUserid();
        this.password = memberSignupForm.getPassword();
        this.nickname = memberSignupForm.getNickname();
        this.create_at = LocalDateTime.now();
    }
    public void update(MemberSignupForm memberSignupForm) {
        if(memberSignupForm.getUserid() != null && !memberSignupForm.getUserid().isBlank()) {
            this.userid = memberSignupForm.getUserid();
        }
        if(memberSignupForm.getPassword() != null && !memberSignupForm.getPassword().isBlank()) {
            this.password = memberSignupForm.getPassword();
        }
        if(memberSignupForm.getNickname() != null && !memberSignupForm.getNickname().isBlank()) {
            this.nickname = memberSignupForm.getNickname();
        }
    }
}
