package uman.tunginside.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uman.tunginside.domain.category.Category;
import uman.tunginside.domain.category.CategoryRegisterForm;
import uman.tunginside.domain.member.Member;
import uman.tunginside.exception.BadRequestException;
import uman.tunginside.repository.CategoryRepository;
import uman.tunginside.repository.MemberRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Transactional
    public Long registerCategory(CategoryRegisterForm categoryRegisterForm, Long member_id) {
        Member member = memberRepository.findById(member_id).orElseThrow(() -> new BadRequestException("없는 회원입니다"));
        // 중복 이름 검색
        if(categoryRepository.existByName(categoryRegisterForm.getName())) {
            throw new BadRequestException("이름이 이미 있습니다");
        }
        // 중복 줄임말 검색
        if(categoryRepository.existsByAbbr(categoryRegisterForm.getAbbr())) {
            throw new BadRequestException("줄임말이 이미 있습니다");
        }
        // 중복 검사 통과라면 저장
        Category category = new Category();
        category.registerCategory(categoryRegisterForm, member);
        categoryRepository.save(category);
        return category.getId();
    }

    @Transactional
    public void deleteCategory(Long member_id, String abbreviation) {
        Member member = memberRepository.findById(member_id).orElseThrow(() -> new BadRequestException("없는 회원입니다"));
        Category category = categoryRepository.findByAbbr(abbreviation)
                .orElseThrow(() -> new BadRequestException("없는 카테코리입니다"));
        if (category.getMember().getId().equals(member.getId())) {
            categoryRepository.delete(category);

        }
        else 
            throw new BadRequestException("권한이 없습니다");
    }
}
