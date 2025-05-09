package uman.tunginside.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

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
}
