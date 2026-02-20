package uman.tunginside.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import uman.tunginside.domain.member.Member;
import uman.tunginside.domain.member.MemberRole;

import java.util.Collection;

@Getter
public class MemberContext extends User {
    private final Long id;
    private final MemberRole memberRole;
    private final Member member;
    public MemberContext(Member member, Collection<? extends GrantedAuthority> authorities) {
        super(member.getUserid(), member.getPassword(), authorities);
        this.id = member.getId();
        this.memberRole = member.getRole();
        this.member = member;
    }
}
