package uman.tunginside.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import uman.tunginside.QuerydslConfig;
import uman.tunginside.domain.category.Category;
import uman.tunginside.domain.category.CategoryRegisterForm;
import uman.tunginside.domain.member.Member;
import uman.tunginside.domain.member.MemberSignupForm;
import uman.tunginside.domain.post.Post;
import uman.tunginside.domain.post.PostWriteForm;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import({MemberRepository.class, CategoryRepository.class, PostRepository.class, QuerydslConfig.class})
class PostRepositoryTest {

    @Autowired private CategoryRepository categoryRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private PostRepository postRepository;

    @Test
    public void countByCondition() {
        //given
        Member member1 = new Member();
        member1.setFromMemberSignupForm(new MemberSignupForm("member1", "member1", "member1"));
        memberRepository.save(member1);
        Category category1 = new Category();
        category1.registerCategory(new CategoryRegisterForm("리그오브레전드", "lol"), member1);
        categoryRepository.save(category1);
        //when
        Post post1 = new Post();
        post1.writePost(new PostWriteForm("lol", "제목1", "콘텐츠1", null), category1, Optional.of(member1), null);
        postRepository.save(post1);
        Post post2 = new Post();
        post2.writePost(new PostWriteForm("lol", "제목2", "콘텐츠2", null), category1, Optional.of(member1), null);
        postRepository.save(post2);
        //then
        assertThat(postRepository.countByCondition(null, null, null)).isEqualTo(2);
        assertThat(postRepository.countByCondition(null, null, "제목")).isEqualTo(2);
        assertThat(postRepository.countByCondition(null, null, "콘텐츠2")).isEqualTo(1);
    }
}