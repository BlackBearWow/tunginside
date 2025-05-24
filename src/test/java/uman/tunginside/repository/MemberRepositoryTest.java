package uman.tunginside.repository;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import uman.tunginside.domain.member.Member;
import uman.tunginside.domain.member.MemberSignupForm;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackages = "uman.tunginside.repository")
class MemberRepositoryTest {

    @Autowired private MemberRepository memberRepository;

    @Test
    public void unique() {
        // given
        Member member = new Member();
        member.setFromMemberSignupForm(new MemberSignupForm("user1", "pass1", "user1"));
        Member member1 = new Member();
        member1.setFromMemberSignupForm(new MemberSignupForm("user1", "pass1", "user1"));
        // when
        memberRepository.save(member);
        // then
        Assertions.assertThrows(ConstraintViolationException.class, () -> memberRepository.save(member1));
    }

    @Test
    void findById() {
        // given
        Member member = new Member();
        member.setFromMemberSignupForm(new MemberSignupForm("user1", "pass1", "user1"));
        memberRepository.save(member);
        // when
        Member member1 = memberRepository.findById(member.getId()).get();
        // then
        assertThat(member1.getNickname()).isEqualTo("user1");
        assertThat(memberRepository.count()).isEqualTo(1L);
    }

    @Test
    public void findByUserId() {
        // given
        Member member = new Member();
        member.setFromMemberSignupForm(new MemberSignupForm("user1", "pass1", "user1"));
        memberRepository.save(member);
        // when
        Member member1 = memberRepository.findByUserid("user1").get();
        // then
        assertThat(member1.getUserid()).isEqualTo("user1");
        assertThat(memberRepository.count()).isEqualTo(1L);
    }

    @Test
    public void existsByUserId() {
        // given
        Member member = new Member();
        member.setFromMemberSignupForm(new MemberSignupForm("user1", "pass1", "user1"));
        memberRepository.save(member);
        // when
        // then
        assertThat(memberRepository.existsByUserid("user1")).isTrue();
        assertThat(memberRepository.count()).isEqualTo(1L);
    }

    @Test
    public void existsByNickname() {
        // given
        Member member = new Member();
        member.setFromMemberSignupForm(new MemberSignupForm("user1", "pass1", "user1"));
        memberRepository.save(member);
        // when
        // then
        assertThat(memberRepository.existsByNickname("user1")).isTrue();
        assertThat(memberRepository.count()).isEqualTo(1L);
    }
}