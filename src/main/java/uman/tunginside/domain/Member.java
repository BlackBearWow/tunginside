package uman.tunginside.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints =  {
        @UniqueConstraint(columnNames = {"userid"}),
        @UniqueConstraint(columnNames = {"nickname"})
})
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @NotNull
    private String userid;
    @NotNull
    private String password;
    @NotNull
    private String nickname;
    @NotNull
    private LocalDateTime create_date;
}
