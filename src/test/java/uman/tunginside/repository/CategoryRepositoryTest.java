package uman.tunginside.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import uman.tunginside.domain.category.Category;
import uman.tunginside.domain.category.CategoryRegisterForm;
import uman.tunginside.domain.member.Member;
import uman.tunginside.domain.member.MemberSignupForm;

@DataJpaTest
@Import({CategoryRepository.class, MemberRepository.class})
class CategoryRepositoryTest {

    @Autowired private CategoryRepository categoryRepository;
    @Autowired private MemberRepository memberRepository;

    @Test
    public void findAll() {
        //given
        Member member1 = new Member();
        member1.setFromMemberSignupForm(new MemberSignupForm("member1", "member1", "member1"));
        memberRepository.save(member1);
        //when
        Category category1 = new Category();
        category1.registerCategory(new CategoryRegisterForm("리그오브레전드", "lol"), member1);
        categoryRepository.save(category1);
        Category category2 = new Category();
        category2.registerCategory(new CategoryRegisterForm("메이플스토리", "mp"), member1);
        categoryRepository.save(category2);
        //then
        Assertions.assertEquals( 2, categoryRepository.findAll().size() );
    }

    @Test
    public void findByAbbr() {
        //given
        Member member1 = new Member();
        member1.setFromMemberSignupForm(new MemberSignupForm("member1", "member1", "member1"));
        memberRepository.save(member1);
        //when
        Category category1 = new Category();
        category1.registerCategory(new CategoryRegisterForm("리그오브레전드", "lol"), member1);
        categoryRepository.save(category1);
        //then
        Assertions.assertEquals(categoryRepository.findByAbbr("lol").get().getName(), category1.getName() );
    }
}