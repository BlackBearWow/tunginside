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
    private MemberRole role;
    private List<Category> categoryList;

    public MemberResponseDTO(Member member) {
        this.userid = member.getUserid();
        this.nickname = member.getNickname();
        this.role = member.getRole();
        this.categoryList = member.getCategoryList();
    }
}
