package uman.tunginside.domain.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import uman.tunginside.domain.member.Member;

@Getter @Setter
@Entity
@Table(
        name = "category",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"}),
        @UniqueConstraint(columnNames = {"abbreviation"})
})
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member member;
    @NotNull @Length(max = 60)
    private String name;
    @NotNull @Length(max = 20)
    private String abbreviation;
}
