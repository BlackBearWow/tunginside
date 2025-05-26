package uman.tunginside.domain.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;
import uman.tunginside.domain.member.Member;

@Getter
@Entity
@Table(
        name = "category",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"}),
        @UniqueConstraint(columnNames = {"abbr"})
})
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    @NotNull @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;
    @NotNull @Length(max = 60)
    private String name;
    @NotNull @Length(max = 20)
    private String abbr;

    public void registerCategory(CategoryRegisterForm categoryRegisterForm, Member member) {
        this.member = member;
        this.name = categoryRegisterForm.getName();
        this.abbr = categoryRegisterForm.getAbbr();
    }
}
