package uman.tunginside.domain.member;

import jakarta.persistence.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import uman.tunginside.domain.category.Category;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @OneToMany(mappedBy = "member")
    private List<Category> categoryList = new ArrayList<>();

    public void setFromMemberSignupForm(MemberSignupForm memberSignupForm) {
        this.userid = memberSignupForm.getUserid();
        this.password = memberSignupForm.getPassword();
        this.nickname = memberSignupForm.getNickname();
        this.create_at = LocalDateTime.now();
    }
    public void update(MemberUpdateForm memberUpdateForm) {
        if(memberUpdateForm.getUserid() != null && !memberUpdateForm.getUserid().isBlank()) {
            this.userid = memberUpdateForm.getUserid();
        }
        if(memberUpdateForm.getNewPassword() != null && !memberUpdateForm.getNewPassword().isBlank()) {
            this.password = memberUpdateForm.getNewPassword();
        }
        if(memberUpdateForm.getNickname() != null && !memberUpdateForm.getNickname().isBlank()) {
            this.nickname = memberUpdateForm.getNickname();
        }
    }
}
