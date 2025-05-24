package uman.tunginside.domain.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import uman.tunginside.domain.category.Category;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class MemberResponseDTO {
    private String userid;
    private String nickname;
    private LocalDateTime create_at;
    private List<Category> categoryList;

    public MemberResponseDTO(Member member) {
        this.userid = member.getUserid();
        this.nickname = member.getNickname();
        this.create_at = member.getCreate_at();
        this.categoryList = member.getCategoryList();
    }
}
