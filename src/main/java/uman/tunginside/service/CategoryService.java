package uman.tunginside.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uman.tunginside.domain.Category;
import uman.tunginside.domain.CategoryRegisterForm;
import uman.tunginside.domain.Member;
import uman.tunginside.exception.BadRequestException;
import uman.tunginside.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public String registerCategory(CategoryRegisterForm categoryRegisterForm, Member member) {
        // 중복 이름 검색
        if(categoryRepository.existByName(categoryRegisterForm.getName())) {
            throw new BadRequestException("이름이 이미 있습니다");
        }
        // 중복 줄임말 검색
        if(categoryRepository.existsByAbbreviation(categoryRegisterForm.getAbbreviation())) {
            throw new BadRequestException("줄임말이 이미 있습니다");
        }
        // 중복 검사 통과라면 저장
        Category category = new Category();
        category.setMember(member);
        category.setName(categoryRegisterForm.getName());
        category.setAbbreviation(categoryRegisterForm.getAbbreviation());
        categoryRepository.save(category);
        return "카테고리 등록 성공";
    }

    public String deleteCategory(Member member, String abbreviation) {
        Category category = categoryRepository.findByAbbreviation(abbreviation)
                .orElseThrow(() -> new BadRequestException("없는 카테코리입니다"));
        if (category.getMember().getId().equals(member.getId())) {
            categoryRepository.delete(category);
            return "카테고리 삭제 성공";
        }
        else 
            throw new BadRequestException("권한이 없습니다");
    }
}
