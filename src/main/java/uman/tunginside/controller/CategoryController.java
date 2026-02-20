package uman.tunginside.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uman.tunginside.domain.category.Category;
import uman.tunginside.domain.category.CategoryDeleteForm;
import uman.tunginside.domain.category.CategoryRegisterForm;
import uman.tunginside.domain.member.Member;
import uman.tunginside.security.MemberContext;
import uman.tunginside.service.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<Category> getCategories() {
        return categoryService.getCategories();
    }

    @PostMapping
    public String createCategory(@RequestBody @Validated CategoryRegisterForm categoryRegisterForm, @AuthenticationPrincipal MemberContext memberContext) {
        categoryService.registerCategory(categoryRegisterForm, memberContext.getId());
        return "카테고리 등록 성공";
    }

    @DeleteMapping
    public String deleteCategory(@AuthenticationPrincipal MemberContext memberContext, @RequestBody @Validated CategoryDeleteForm categoryDeleteForm) {
        categoryService.deleteCategory(memberContext.getId(), categoryDeleteForm.getAbbr());
        return "카테고리 삭제 성공";
    }

    @GetMapping("/admin")
    public List<Category> getCategoriesAdmin() {
        return categoryService.getCategories();
    }
}
